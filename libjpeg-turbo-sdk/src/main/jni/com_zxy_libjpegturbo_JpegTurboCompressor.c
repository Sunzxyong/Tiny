//
// Created by zhengxiaoyong on 2017/2/23.
//
#include <string.h>
#include <android/bitmap.h>
#include <android/log.h>
#include <stdio.h>
#include <setjmp.h>
#include <math.h>
#include "include/jpeglib.h"
#include "include/cdjpeg.h"

//
// Native interface, it will be call in java code
//

#define LOG_TAG "jni"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG ,__VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,LOG_TAG,__VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,LOG_TAG ,__VA_ARGS__)

#define R16_BITS 5
#define G16_BITS 6
#define B16_BITS 5

#define R32_BITS 8
#define G32_BITS 8
#define B32_BITS 8
#define A32_BITS 8

#define RGB_565_BITS 16
#define BGRA_8888_BITS 32

/*
 * the extended error handler struct
 */
struct my_error_mgr {
    struct jpeg_error_mgr pub;
    jmp_buf setjmp_buffer;
};

typedef struct my_error_mgr *my_error_ptr;

/*
 * color components per pixel
 */
int components = 3;
/*
 * color space of input image
 */
J_COLOR_SPACE color_space = JCS_RGB;//default RGB_888,24bit

uint8_t *raw_image = NULL;

/*
 * Here's the routine that will replace the standard error_exit method
 */
METHODDEF(void)
my_error_exit(j_common_ptr cinfo) {
    my_error_ptr myerr = (my_error_ptr) cinfo->err;
    (*cinfo->err->output_message)(cinfo);

    LOGE("jpeg_message_table[%d]:%s", myerr->pub.msg_code,
         myerr->pub.jpeg_message_table[myerr->pub.msg_code]);

    /* Return control to the setjmp point */
    longjmp(myerr->setjmp_buffer, 1);

}

int compress(uint8_t *image_buffer, int width, int height, int quality,
             const char *filename, jboolean optimize) {

    struct jpeg_compress_struct cinfo;

    struct my_error_mgr jerr;

    JSAMPROW row_pointer[1];

    FILE *outfile = NULL;

    int row_stride;

    //set up the error handler first, in case the initialization step fails
    cinfo.err = jpeg_std_error(&jerr.pub);
    jerr.pub.error_exit = my_error_exit;
    if (setjmp(jerr.setjmp_buffer)) {
        /* If we get here, the JPEG code has signaled an error.
         * We need to clean up the JPEG object, close the file, and return.
         */
        jpeg_destroy_compress(&cinfo);
        if (outfile != NULL)
            fclose(outfile);
        return 0;
    }

    //allocate and initialize JPEG compression objec
    jpeg_create_compress(&cinfo);

    outfile = fopen(filename, "wb");
    if (outfile == NULL) {
        LOGE("Cannot open %s !", filename);
        return 0;
    }
    //config data destination
    jpeg_stdio_dest(&cinfo, outfile);

    cinfo.image_width = (JDIMENSION) width;
    cinfo.image_height = (JDIMENSION) height;
    //TRUE=arithmetic coding, FALSE=Huffman
    cinfo.arith_code = FALSE;
    cinfo.input_components = components;

    //colorspace of input image
    cinfo.in_color_space = color_space;

    cinfo.input_gamma = 1;

    //set parameters for compression
    jpeg_set_defaults(&cinfo);

    //use Huffman
    cinfo.optimize_coding = optimize;

    jpeg_set_quality(&cinfo, quality, TRUE);

    //start compressor
    jpeg_start_compress(&cinfo, TRUE);

    row_stride = cinfo.image_width * components;//JSAMPLEs per row in image_buffer
    /* while (scan lines remain to be written)
     *
     * jcs.next_scanline:0~image_height-1
     * */
    while (cinfo.next_scanline < cinfo.image_height) {
        row_pointer[0] = &image_buffer[cinfo.next_scanline * row_stride];

        (void) jpeg_write_scanlines(&cinfo, row_pointer, 1);
    }

    if (cinfo.optimize_coding) {
        LOGI("Huffman optimize==true");
    } else {
        LOGI("Huffman optimize==false");
    }
    //finish compression
    jpeg_finish_compress(&cinfo);
    //release JPEG compression object
    jpeg_destroy_compress(&cinfo);
    //close the output file
    fclose(outfile);

    return 1;
}

int decompress(const char *filename) {

    struct jpeg_decompress_struct cinfo;

    struct my_error_mgr jerr;

    FILE *infile = NULL;// source file

    JSAMPARRAY buffer;// Output row buffer

    int row_stride;// physical row width in output buffer

    //set up the error handler first, in case the initialization step fails
    cinfo.err = jpeg_std_error(&jerr.pub);
    jerr.pub.error_exit = my_error_exit;
    if (setjmp(jerr.setjmp_buffer)) {
        /* If we get here, the JPEG code has signaled an error.
         * We need to clean up the JPEG object, close the input file, and return.
         */
        jpeg_destroy_decompress(&cinfo);
        if (infile != NULL)
            fclose(infile);
        return 0;
    }

    infile = fopen(filename, "rb");
    if (infile == NULL) {
        LOGE("Cannot open %s !", filename);
        return 0;
    }

    /* Now we can initialize the JPEG decompression object. */
    jpeg_create_decompress(&cinfo);

    jpeg_stdio_src(&cinfo, infile);

    jpeg_read_header(&cinfo, TRUE);

    jpeg_start_decompress(&cinfo);

    row_stride = cinfo.output_width * cinfo.output_components;

    buffer = (*cinfo.mem->alloc_sarray)
            ((j_common_ptr) &cinfo, JPOOL_IMAGE, row_stride, 1);
    int i = 0;
    while (cinfo.output_scanline < cinfo.output_height) {
        /* jpeg_read_scanlines expects an array of pointers to scanlines.
         * Here the array is only one element long, but you could ask for
         * more than one scanline at a time if that's more convenient.
         */
        jpeg_read_scanlines(&cinfo, buffer, 1);
        for (i = 0; i < row_stride; i++) {
            raw_image[i] = buffer[0][i];
        }
    }

    jpeg_finish_decompress(&cinfo);
    jpeg_destroy_decompress(&cinfo);
    fclose(infile);

    return 1;
}

/*
 * Class:     com_zxy_libjpegturbo_JpegTurboCompressor
 * Method:    nativeCompress
 * Signature: (Landroid/graphics/Bitmap;Ljava/lang/String;IZ)Z
 */
JNIEXPORT jboolean JNICALL Java_com_zxy_libjpegturbo_JpegTurboCompressor_nativeCompress
        (JNIEnv *env, jclass jclazz, jobject jbitmap, jstring joutput, jint jquality,
         jboolean joptimize) {

    AndroidBitmapInfo targetInfo;
    int result;
    uint8_t *targetPixel;
    uint8_t *data;
    uint8_t *tmpData;
    uint32_t width;
    u_int32_t height;

    if (joutput == NULL) {
        LOGE("Cannot get output file !");
        return (jboolean) JNI_FALSE;
    }

    const char *outfile = (*env)->GetStringUTFChars(env, joutput, NULL);

    result = AndroidBitmap_getInfo(env, jbitmap, &targetInfo);

    if (result < 0) {
        LOGE("Cannot get bitmap info ! result=%d", result);
        (*env)->ReleaseStringUTFChars(env, joutput, outfile);
        return (jboolean) JNI_FALSE;
    }

    width = targetInfo.width;
    height = targetInfo.height;

    result = AndroidBitmap_lockPixels(env, jbitmap, (void **) &targetPixel);

    if (targetInfo.format == ANDROID_BITMAP_FORMAT_RGB_565) {
        LOGE("Bitmap format : RGB_565, width:%d, height:%d", targetInfo.width, targetInfo.height);
    } else if (targetInfo.format == ANDROID_BITMAP_FORMAT_RGBA_8888) {
        LOGE("Bitmap format : RGBA_8888, width:%d, height:%d", targetInfo.width, targetInfo.height);
    } else if (targetInfo.format == ANDROID_BITMAP_FORMAT_A_8) {
        LOGE("Bitmap format : A_8, width:%d, height:%d", targetInfo.width, targetInfo.height);
    } else if (targetInfo.format == ANDROID_BITMAP_FORMAT_RGBA_4444) {
        LOGE("Bitmap format : RGBA_4444, width:%d, height:%d", targetInfo.width, targetInfo.height);
    }

    if (result < 0) {
        LOGE("Cannot lock bitmap pixels ! result=%d", result);
        (*env)->ReleaseStringUTFChars(env, joutput, outfile);
        return (jboolean) JNI_FALSE;
    }

    data = malloc((size_t) (width * height * components));
    tmpData = data;

    uint8_t r, g, b;
    int i=0;
    int j=0;

    switch (targetInfo.format) {
        case ANDROID_BITMAP_FORMAT_RGB_565:
            //covert to RGB_888 24bit.r g b
            for (i = 0; i < height; i++) {
                for (j = 0; j < width; j++) {
                    int color = *((int *) targetPixel);
                    r = (uint8_t) ((color & 0xF800) >> (RGB_565_BITS - R16_BITS));
                    g = (uint8_t) ((color & 0x07E0) >> (RGB_565_BITS - R16_BITS - G16_BITS));
                    b = (uint8_t) ((color & 0x001F) >> 0);

                    r = (r << (R32_BITS - R16_BITS)) | (r >> (2 * R16_BITS - R32_BITS));
                    g = (g << (G32_BITS - G16_BITS)) | (g >> (2 * G16_BITS - G32_BITS));
                    b = (b << (B32_BITS - B16_BITS)) | (b >> (2 * B16_BITS - B32_BITS));

                    data[0] = r;
                    data[1] = g;
                    data[2] = b;

                    data += 3;
                    //next pixel point
                    targetPixel += 2;
                }
            }
            break;
        case ANDROID_BITMAP_FORMAT_RGBA_8888:
            //filter alpha channel.b g r a
            for (i = 0; i < height; i++) {
                for (j = 0; j < width; j++) {
                    int color = *((int *) targetPixel);
                    //a = (uint8_t) ((color & 0xFF000000) >> (BGRA_8888_BITS - A32_BITS));
                    r = (uint8_t) ((color & 0x00FF0000) >> (BGRA_8888_BITS - A32_BITS - R32_BITS));
                    g = (uint8_t) ((color & 0x0000FF00) >> (BGRA_8888_BITS - A32_BITS - R32_BITS - G32_BITS));
                    b = (uint8_t) ((color & 0x000000FF) >> 0);

                    data[0] = b;
                    data[1] = g;
                    data[2] = r;

                    data += 3;
                    //next pixel point
                    targetPixel += 4;
                }
            }
            break;
        case ANDROID_BITMAP_FORMAT_A_8:
        case ANDROID_BITMAP_FORMAT_RGBA_4444:
            //ignore
            break;
        default:
            break;
    }

    result = AndroidBitmap_unlockPixels(env, jbitmap);

    if (result < 0) {
        LOGE("Cannot unlock bitmap pixels ! result=%d", result);
    }

    int resultCode = compress(tmpData, width, height, jquality, outfile, joptimize);

    free(tmpData);

    if (resultCode == 0) {
        LOGE("Cannot write bitmap info !");
        (*env)->ReleaseStringUTFChars(env, joutput, outfile);
        return (jboolean) JNI_FALSE;
    }

    (*env)->ReleaseStringUTFChars(env, joutput, outfile);

    return (jboolean) JNI_TRUE;
}

/*
 * Class:     com_zxy_libjpegturbo_JpegTurboCompressor
 * Method:    getLibjpegTurboVersion
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_zxy_libjpegturbo_JpegTurboCompressor_getLibjpegTurboVersion
        (JNIEnv *env, jclass jclazz) {
    return LIBJPEG_TURBO_VERSION_NUMBER;
}

/*
 * Class:     com_zxy_libjpegturbo_JpegTurboCompressor
 * Method:    getLibjpegVersion
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_zxy_libjpegturbo_JpegTurboCompressor_getLibjpegVersion
        (JNIEnv *env, jclass jclazz) {
    return JPEG_LIB_VERSION;
}