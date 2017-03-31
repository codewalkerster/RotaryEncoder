#include <jni.h>
#include <android/log.h>

#define LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define LOG_TAG "rotary_encoder"

#include <stdio.h>
#include <stdarg.h>
#include <stdint.h>
#include <stdlib.h>
#include <ctype.h>
#include <poll.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <time.h>
#include <fcntl.h>
#include <pthread.h>
#include <sys/time.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <sys/wait.h>
#include <sys/ioctl.h>
#include <linux/input.h>

//------------------------------------------------------------------------------
//
// Global handle Define
//
//------------------------------------------------------------------------------
//
// Encoder Config
// 1. Board Select, 2. keycode define, 3. port level define, 4. gpio define
//
//------------------------------------------------------------------------------
//#define	BOARD_ODROID_C
#define	BOARD_ODROID_C2
//#define	BOARD_ODROID_XU3

/* keycode define -> linux/input.h */
#define	ENCODEER_CCW_KEYCODE		KEY_VOLUMEDOWN
#define	ENCODEER_CW_KEYCODE		KEY_VOLUMEUP
#define	ENCODEER_PUSH_BT_KEYCODE	KEY_MUTE

//------------------------------------------------------------------------------
#define	PORT_LEVEL	1	/* Active level */
#define	PUSH_BT_LEVEL	1	/* Active level */
#define	PULL_UPDN_EN	1	/* internal pull up/down enable */

//------------------------------------------------------------------------------
#if defined(BOARD_ODROID_C)

#define	GPIO_PORT_A	102	/* GPIOX.5 */
#define	GPIO_PORT_B	104	/* GPIOX.7 */
#define	GPIO_PUSH_BT	103	/* GPIOX.6 */

#endif

#if defined(BOARD_ODROID_C2)

#define	GPIO_PORT_A	233	/* GPIOX.5 */
#define	GPIO_PORT_B	236	/* GPIOX.8 */
#define	GPIO_PUSH_BT	231	/* GPIOX.3 */

#endif

#if defined(BOARD_ODROID_XU3)

#define	GPIO_PORT_A	23	/* GPX1.7 */
#define	GPIO_PORT_B	19	/* GPX1.3 */
#define	GPIO_PUSH_BT	24	/* GPX2.0 */

#endif

struct port_info {
	unsigned int	gpio;
	unsigned int	active;
	unsigned int	pullen;
};

struct encoder_config {
	/* gpio port description */
	struct port_info port_a;
	struct port_info port_b;
	struct port_info push_bt;

	/* Encoder dir : Counter Clockwise keycode */
	unsigned int ccw_keycode;
	/* Encoder dir : Clockwise keycode */
	unsigned int cw_keycode;
	/* Push button keycode */
	unsigned int push_bt_keycode;
};

#define	ENCODER_IOCSREG	_IOW('e', 1, struct encoder_config)
#define	ENCODER_IOCGREG	_IOR('e', 2, int)

int	fd;

int init_rotaryencoder(void)
{
	if ((fd = open ("/dev/odroid-encoder", O_RDWR | O_SYNC | O_CLOEXEC) ) < 0) {
		LOGI("/dev/odroid-encoder open error!");
		return -1;
	}

	return	0;
}

JNIEXPORT int Java_com_hardkernel_odroid_rotaryencoder_MainActivity_setRotaryEncoder(JNIEnv* env, jobject thiz,
        jint port_a_gpio, jint port_a_active, jint port_a_pullupdn, jint port_a_keycode,
        jint port_b_gpio, jint port_b_active, jint port_b_pullupdn, jint port_b_keycode,
        jint button_gpio, jint button_active, jint button_pullupdn, jint button_keycode)
{
	struct encoder_config	encoder_cfg;
	int	status, ret;

	if (init_rotaryencoder() < 0) {
		LOGI("%s: Init rotary encoder failed", __func__);
		close(fd);
		return -1;
	}

	memset(&encoder_cfg, 0x00, sizeof(struct encoder_config));

	encoder_cfg.port_a.gpio   = port_a_gpio;
	encoder_cfg.port_a.active = port_a_active;
	encoder_cfg.port_a.pullen = port_a_pullupdn;
	encoder_cfg.ccw_keycode = port_a_keycode;

	encoder_cfg.port_b.gpio   = port_b_gpio;
	encoder_cfg.port_b.active = port_b_active;
	encoder_cfg.port_b.pullen = port_b_pullupdn;
	encoder_cfg.cw_keycode  = port_b_keycode;

	encoder_cfg.push_bt.gpio   = button_gpio;
	encoder_cfg.push_bt.active = button_active;
	encoder_cfg.push_bt.pullen = button_pullupdn;
	encoder_cfg.push_bt_keycode = button_keycode;

#if defined(DISABLE_INTERNAL_PULLUPDN)
	encoder_cfg.port_a.pullen  = 0;
	encoder_cfg.port_b.pullen  = 0;
	encoder_cfg.push_bt.pullen = 0;
#endif

	ret = ioctl(fd, ENCODER_IOCGREG, &status);
	LOGI("encoder ioctl get status : ret = %d, status = %d", ret, status);

	ret = ioctl(fd, ENCODER_IOCSREG, &encoder_cfg);
	LOGI("encoder ioctl set config : ret = %d", ret);

	close(fd);
	return 0;
}
