package com.zxventures.beer.utils;

/**
 * Responsable to enable/disable all the application Log messages in order to
 * bump the runtime performance.
 */
public class Log {

    /**
     * Development Mode True, Production mode False
     */
    static final boolean LOG = true;

    /**
     * Information
     *  @param tag    the tag
     *
     */
    public static void i(final String tag, final String string) {
        if (LOG) {
            android.util.Log.i(tag, string);
        }
    }

    /**
     * Information
     *
     * @param tag    the tag
     * @param string the string
     * @param e      the exception
     */
    public static void i(final String tag, final String string,
                         final Throwable e) {
        if (LOG) {
            android.util.Log.i(tag, string, e);
        }
    }

    /**
     * Error
     *
     * @param tag    the tag
     * @param string the string
     */
    public static void e(final String tag, final String string) {
        if (LOG) {
            android.util.Log.e(tag, string);
        }
    }

    /**
     * Error
     *
     * @param tag    the tag
     * @param string the string
     * @param e      the exception
     */
    public static void e(final String tag, final String string,
                         final Throwable e) {
        if (LOG) {
            android.util.Log.e(tag, string, e);
        }
    }

    /**
     * Debug
     *
     * @param tag    the tag
     * @param string the string
     */
    public static void d(final String tag, final String string) {
        if (LOG) {
            android.util.Log.d(tag, string);
        }
    }

    /**
     * Debug
     *
     * @param tag    the tag
     * @param string the string
     * @param e      the exception
     */
    public static void d(final String tag, final String string,
                         final Throwable e) {
        if (LOG) {
            android.util.Log.d(tag, string, e);
        }
    }

    /**
     * Verbose
     *
     * @param tag    the tag
     * @param string the string
     */
    public static void v(final String tag, final String string) {
        if (LOG) {
            android.util.Log.v(tag, string);
        }
    }

    /**
     * Verbose
     *
     * @param tag    the tag
     * @param string the string
     * @param e      the exception
     */
    public static void v(final String tag, final String string,
                         final Throwable e) {
        if (LOG) {
            android.util.Log.v(tag, string, e);
        }
    }

    /**
     * Warning
     *
     * @param tag    the tag
     * @param string the string
     */
    public static void w(final String tag, final String string) {
        if (LOG) {
            android.util.Log.w(tag, string);
        }
    }

    /**
     * Warning
     *
     * @param tag    the tag
     * @param string the string
     * @param e      the exception
     */
    public static void w(final String tag, final String string,
                         final Throwable e) {
        if (LOG) {
            android.util.Log.e(tag, string, e);
        }
    }

}
