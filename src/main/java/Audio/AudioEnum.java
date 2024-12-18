package Audio;

public enum AudioEnum {

    THEME("wavYachtTheme.wav"),
    SHOT1("wavShot1.wav"),
    SHOT2("wavShot2.wav"),
    HIT_SMALL("wavHitSmall.wav"),
    HIT_DISTANT("wavHitDistant.wav"),
    HIT_FINAL("wavHitFinal.wav"),
    YES("wavYes.wav"),
    NO("wavNo.wav"),
    CLACK1("wavClack1.wav"),
    CLACK2("wavClack2.wav"),
    CLACK3("wavClack3.wav"),
    CLACK4("wavClack4.wav"),
    CLACK5("wavClack5.wav"),
    CLACK6("wavClack6.wav"),
    CLACK7("wavClack7.wav"),

    SPLASH1("wavSplash1.wav"),
    SPLASH2("wavSplash2.wav"),
    SPLASH3("wavSplash3.wav"),
    SPLASH4("wavSplash4.wav");

    private final String filePath;

    AudioEnum(String filePath) {
        this.filePath = filePath;

    }
    public String getFilePath(){
        return filePath;
    }
}