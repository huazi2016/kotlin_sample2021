/**
 * author : huazi
 * time   : 2021/4/13
 * desc   :
 */
object AndroidConfig {

    val compileSdkVersion = 30
    val buildToolsVersion = "30.0.2"
    val defaultConfig = DefaultConfig()

    class DefaultConfig {
        val applicationId = "com.test.huazi"
        val minSdkVersion = 19
        val targetSdkVersion = 30
        val versionCode = 1011
        val versionName = "10.1.1"
    }
}