package com.fight2048.oss;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Utils {

    /**
     * 数组中元素未找到的下标，值为-1
     */
    public static final int INDEX_NOT_FOUND = -1;

    /**
     * 数组是否为非空
     *
     * @param <T>   数组元素类型
     * @param array 数组
     * @return 是否为非空
     */
    public static <T> boolean isNotEmpty(T[] array) {
        return (array != null && array.length != 0);
    }


    /**
     * 数组中是否包含元素
     *
     * @param <T>   数组元素类型
     * @param array 数组
     * @param value 被检查的元素
     * @return 是否包含
     */
    public static <T> boolean contains(T[] array, T value) {
        return indexOf(array, value) > INDEX_NOT_FOUND;
    }

    /**
     * 返回数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
     *
     * @param <T>   数组类型
     * @param array 数组
     * @param value 被检查的元素
     * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
     * @since 3.0.7
     */
    public static <T> int indexOf(T[] array, Object value) {
        if (null != array) {
            for (int i = 0; i < array.length; i++) {
                if (Objects.equals(value, array[i])) {
                    return i;
                }
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * 字符串是否为空白 空白的定义如下： <br>
     * 1、为null <br>
     * 2、为不可见字符（如空格）<br>
     * 3、""<br>
     *
     * @param str 被检测的字符串
     * @return 是否为空
     */
    public static boolean isBlank(CharSequence str) {
        int length;

        if ((str == null) || ((length = str.length()) == 0)) {
            return true;
        }

        for (int i = 0; i < length; i++) {
            // 只要有一个非空字符即为非空字符串
            if (!isBlankChar(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * 是否空白符<br>
     * 空白符包括空格、制表符、全角空格和不间断空格<br>
     *
     * @param c 字符
     * @return 是否空白符
     * @see Character#isWhitespace(int)
     * @see Character#isSpaceChar(int)
     * @since 4.0.10
     */
    public static boolean isBlankChar(int c) {
        return Character.isWhitespace(c) || Character.isSpaceChar(c) || c == '\ufeff' || c == '\u202a';
    }

    /**
     * 获取文件后缀名
     *
     * @param fileName 文件名
     * @return 文件后缀名
     */
    public static String fileExt(String fileName) {
        if (isBlank(fileName) || !fileName.contains(".")) {
            return null;
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 字符串是否为空，空的定义如下:<br>
     * 1、为null <br>
     * 2、为""<br>
     *
     * @param str 被检测的字符串
     * @return 是否为空
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * 获取媒体类型
     *
     * @param fileName 文件名
     * @return 媒体类型
     */
    public static String mimeType(String fileName) {
        String ext = fileExt(fileName);
        if (null == ext) {
            return null;
        }
        return MIME_TYPES.getOrDefault(ext, APPLICATION);
    }

    public static final String APPLICATION = "application/octet-stream";

    public static final Map<String, String> MIME_TYPES = new HashMap<String, String>() {{
        put("kar", "audio/midi");
        put("mid", "audio/midi");
        put("midi", "audio/midi");

        put("aac", "audio/mp4");
        put("f4a", "audio/mp4");
        put("f4b", "audio/mp4");
        put("m4a", "audio/mp4");

        put("mp3", "audio/mpeg");
        put("oga", "audio/ogg");
        put("ogg", "audio/ogg");
        put("opus", "audio/ogg");

        put("ra", "audio/x-realaudio");
        put("wav", "audio/x-wav");

        put("bmp", "image/bmp");
        put("gif", "image/gif");
        put("jpeg", "image/jpeg");
        put("jpg", "image/jpeg");

        put("png", "image/png");
        put("svg", "image/svg+xml");
        put("svgz", "image/svg+xml");

        put("tif", "image/tiff");
        put("tiff", "image/tiff");
        put("wbmp", "image/vnd.wap.wbmp");
        put("webp", "image/webp");
        put("ico", "image/x-icon");
        put("cur", "image/x-icon");
        put("jng", "image/x-jng");

        put("js", "application/javascript");
        put("json", "application/json");

        put("webapp", "application/x-web-app-manifest+json");
        put("manifest", "text/cache-manifest");
        put("appcache", "text/cache-manifest");

        put("doc", "application/msword");
        put("xls", "application/vnd.ms-excel");
        put("ppt", "application/vnd.ms-powerpoint");
        put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");

        put("3gpp", "video/3gpp");
        put("3gp", "video/3gpp");

        put("mp4", "video/mp4");
        put("m4v", "video/mp4");
        put("f4v", "video/mp4");
        put("f4p", "video/mp4");

        put("mpeg", "video/mpeg");
        put("mpg", "video/mpeg");

        put("ogv", "video/ogg");
        put("mov", "video/quicktime");
        put("webm", "video/webm");
        put("flv", "video/x-flv");
        put("mng", "video/x-mng");
        put("asx", "video/x-ms-asf");
        put("asf", "video/x-ms-asf");

        put("wmv", "video/x-ms-wmv");
        put("avi", "video/x-msvideo");

        put("atom", "application/xml");
        put("rdf", "application/xml");
        put("rss", "application/xml");
        put("xml", "application/xml");

        put("woff", "application/font-woff");
        put("woff2", "application/font-woff2");
        put("eot", "application/vnd.ms-fontobject");
        put("ttc", "application/x-font-ttf");
        put("ttf", "application/x-font-ttf");

        put("otf", "font/opentype");

        put("jar", "application/java-archive");
        put("war", "application/java-archive");
        put("ear", "application/java-archive");

        put("hqx", "application/mac-binhex40");
        put("pdf", "application/pdf");
        put("ps", "application/postscript");
        put("eps", "application/postscript");
        put("ai", "application/postscript");

        put("rtf", "application/rtf");
        put("wmlc", "application/vnd.wap.wmlc");
        put("xhtml", "application/xhtml+xml");
        put("kml", "application/vnd.google-earth.kml+xml");
        put("kmz", "application/vnd.google-earth.kmz");
        put("7z", "application/x-7z-compressed");
        put("crx", "application/x-chrome-extension");
        put("oex", "application/x-opera-extension");
        put("xpi", "application/x-xpinstall");
        put("cco", "application/x-cocoa");
        put("jardiff", "application/x-java-archive-diff");
        put("jnlp", "application/x-java-jnlp-file");
        put("run", "application/x-makeself");

        put("pl", "application/x-perl");
        put("pm", "application/x-perl");

        put("prc", "application/x-pilot");
        put("pdb", "application/x-pilot");

        put("rar", "application/x-rar-compressed");
        put("rpm", "application/x-redhat-package-manager");
        put("sea", "application/x-sea");
        put("swf", "application/x-shockwave-flash");
        put("sit", "application/x-stuffit");
        put("tcl", "application/x-tcl");
        put("tk", "application/x-tcl");

        put("der", "application/x-x509-ca-cert");
        put("pem", "application/x-x509-ca-cert");
        put("crt", "application/x-x509-ca-cert");

        put("torrent", "application/x-bittorrent");
        put("zip", "application/zip");

        put("bin", "application/octet-stream");
        put("exe", "application/octet-stream");
        put("dll", "application/octet-stream");

        put("deb", "application/octet-stream");
        put("dmg", "application/octet-stream");
        put("iso", "application/octet-stream");
        put("img", "application/octet-stream");

        put("msi", "application/octet-stream");
        put("msp", "application/octet-stream");
        put("msm", "application/octet-stream");

        put("safariextz", "application/octet-stream");

        put("css", "text/css");
        put("html", "text/html");
        put("htm", "text/html");
        put("shtml", "text/html");

        put("mml", "text/mathml");
        put("txt", "text/plain");
        put("jad", "text/vnd.sun.j2me.app-descriptor");
        put("wml", "text/vnd.wap.wml");
        put("vtt", "text/vtt");
        put("htc", "text/x-component");
        put("vcf", "text/x-vcard");
    }};
}
