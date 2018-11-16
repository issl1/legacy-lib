package org.seuksa.frmk.tools.spring;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;

/**
 * 
 * @author prasnar
 *
 */
public class SpringMobileUtils {
	private static Logger logger = LoggerFactory.getLogger(SpringMobileUtils.class);

	/**
     * 
     * @param httpRequest
     * @return
     */
    public static boolean clientDeviceIsMobile(HttpServletRequest httpRequest) {
		String userAgent = httpRequest.getHeader("User-Agent").toLowerCase();

		Device currentDevice = DeviceUtils.getCurrentDevice(httpRequest);
		if (currentDevice == null) {
        	logger.info("**SPRING**DEVICE IS NULL=>NORMAL* userAgent [" + userAgent  + "]*************");
        	return false;
		}
		if (currentDevice.isMobile()) {
			logger.info("**SPRING**DEVICE IS MOBILE* userAgent [" + userAgent  + "]*************");
			return true;
        } else if (currentDevice.isTablet()) {
            logger.info("**SPRING**DEVICE IS TABLET* userAgent [" + userAgent  + "]*************");
            return false;
        } else {
        	logger.info("**SPRING**DEVICE IS NORMAL* userAgent [" + userAgent  + "]*************");
        	return false;
        }
    }
    
    /**
     * 
     * @param httpRequest
     * @return
     */
    public static boolean clientDeviceIsTablet(HttpServletRequest httpRequest) {
		String userAgent = httpRequest.getHeader("User-Agent").toLowerCase();

		Device currentDevice = DeviceUtils.getCurrentDevice(httpRequest);
		if (currentDevice == null) {
        	logger.info("**SPRING**DEVICE IS NULL=>NORMAL* userAgent [" + userAgent  + "]*************");
        	return false;
		}
		if (currentDevice.isMobile()) {
			logger.info("**SPRING**DEVICE IS MOBILE* userAgent [" + userAgent  + "]*************");
			return false;
        } else if (currentDevice.isTablet()) {
            logger.info("**SPRING**DEVICE IS TABLET* userAgent [" + userAgent  + "]*************");
            return true;
        } else {
        	logger.info("**SPRING**DEVICE IS NORMAL* userAgent [" + userAgent  + "]*************");
        	return false;
        }
    }
    
    /**
     * 
     * @param httpRequest
     * @return
     */
    public static boolean clientDeviceIsNormal(HttpServletRequest httpRequest) {
		String userAgent = httpRequest.getHeader("User-Agent").toLowerCase();

		Device currentDevice = DeviceUtils.getCurrentDevice(httpRequest);
		if (currentDevice == null) {
        	logger.info("**SPRING**DEVICE IS NULL=>NORMAL* userAgent [" + userAgent  + "]*************");
        	return false;
		}
		if (currentDevice.isMobile()) {
			logger.info("**SPRING**DEVICE IS MOBILE* userAgent [" + userAgent  + "]*************");
			return false;
        } else if (currentDevice.isTablet()) {
            logger.info("**SPRING**DEVICE IS TABLET* userAgent [" + userAgent  + "]*************");
            return false;
        } else {
        	logger.info("**SPRING**DEVICE IS NORMAL* userAgent [" + userAgent  + "]*************");
        	return true;
        }
    }
    
    /**
     * 
     * @param httpRequest
     * @return
     */
    public static boolean clientDeviceIsMobileOrTablet(HttpServletRequest httpRequest) {
		String userAgent = httpRequest.getHeader("User-Agent").toLowerCase();

		Device currentDevice = DeviceUtils.getCurrentDevice(httpRequest);
		if (currentDevice == null) {
        	logger.info("**SPRING**DEVICE IS NULL=>NORMAL* userAgent [" + userAgent  + "]*************");
        	return false;
		}
		if (currentDevice.isMobile()) {
			logger.info("**SPRING**DEVICE IS MOBILE* userAgent [" + userAgent  + "]*************");
			return true;
        } else if (currentDevice.isTablet()) {
            logger.info("**SPRING**DEVICE IS TABLET* userAgent [" + userAgent  + "]*************");
            return true;
        } else {
        	logger.info("**SPRING**DEVICE IS NORMAL* userAgent [" + userAgent  + "]*************");
        	return false;
        }
    }
}
