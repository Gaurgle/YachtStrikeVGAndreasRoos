package ServerSide;

import Audio.AudioEnum;

public class AudioManager {

    private static AudioManager instance;

    private Audio audioSongYachtTheme;
    private Audio audioShot1, audioShot2;
    private Audio audioHitSmall, audioHitMedium, audioHitFinal;
    private Audio audioYes, audioNo;
    private Audio audioClack1, audioClack2, audioClack3, audioClack4, audioClack5, audioClack6, audioClack7;
    private Audio audioSplash1, audioSplash2, audioSplash3, audioSplash4;
    private Audio voiceJP1;
    private Audio voiceBez1, voiceBez2;
    private Audio voiceMusk1, voiceMusk2;
    private Audio voiceTrump1, voiceTrump2;
    private Audio voiceWarren1;

    private AudioManager() {
        try {
            audioSongYachtTheme = new Audio(AudioEnum.THEME.getFilePath(), true, AudioEnum.THEME);
            audioShot1 = new Audio(AudioEnum.SHOT1.getFilePath(), false, AudioEnum.SHOT1);
            audioShot2 = new Audio(AudioEnum.SHOT2.getFilePath(), false, AudioEnum.SHOT2);
            audioHitSmall = new Audio(AudioEnum.HIT_SMALL.getFilePath(), false, AudioEnum.HIT_SMALL);
            audioHitMedium = new Audio(AudioEnum.HIT_MEDIUM.getFilePath(), false, AudioEnum.HIT_MEDIUM);
            audioHitFinal = new Audio(AudioEnum.HIT_FINAL.getFilePath(), false, AudioEnum.HIT_FINAL);
            audioSplash1 = new Audio(AudioEnum.SPLASH1.getFilePath(), false, AudioEnum.SPLASH1);
            audioSplash2 = new Audio(AudioEnum.SPLASH2.getFilePath(), false, AudioEnum.SPLASH2);
            audioSplash3 = new Audio(AudioEnum.SPLASH3.getFilePath(), false, AudioEnum.SPLASH3);
            audioSplash4 = new Audio(AudioEnum.SPLASH4.getFilePath(), false, AudioEnum.SPLASH4);
            audioYes = new Audio(AudioEnum.YES.getFilePath(), false, AudioEnum.YES);
            audioYes.setVolume(-6.0f,1,10);
            audioNo = new Audio(AudioEnum.NO.getFilePath(), false, AudioEnum.NO);
            audioNo.setVolume(-6.0f,1,10);
            audioClack1 = new Audio(AudioEnum.CLACK1.getFilePath(), false, AudioEnum.CLACK1);
            audioClack2 = new Audio(AudioEnum.CLACK2.getFilePath(),false, AudioEnum.CLACK2);
            audioClack3 = new Audio(AudioEnum.CLACK3.getFilePath(),false, AudioEnum.CLACK3);
            audioClack4 = new Audio(AudioEnum.CLACK4.getFilePath(),false, AudioEnum.CLACK4);
            audioClack5 = new Audio(AudioEnum.CLACK5.getFilePath(),false, AudioEnum.CLACK5);
            audioClack6 = new Audio(AudioEnum.CLACK6.getFilePath(),false, AudioEnum.CLACK6);
            audioClack7 = new Audio(AudioEnum.CLACK7.getFilePath(),false, AudioEnum.CLACK7);

            voiceJP1 = new Audio(AudioEnum.JP1.getFilePath(), false, AudioEnum.JP1);
            voiceBez1 = new Audio(AudioEnum.BEZ1.getFilePath(), false,AudioEnum.BEZ1);
            voiceBez2 = new Audio(AudioEnum.BEZ2.getFilePath(),false,AudioEnum.BEZ2);
            voiceMusk1 = new Audio(AudioEnum.MUSK1.getFilePath(), false, AudioEnum.MUSK1);
            voiceMusk2 = new Audio(AudioEnum.MUSK2.getFilePath(), false, AudioEnum.MUSK2);
            voiceTrump1 = new Audio(AudioEnum.TRUMP1.getFilePath(), false, AudioEnum.TRUMP1);
            voiceTrump2 = new Audio(AudioEnum.TRUMP2.getFilePath(), false, AudioEnum.TRUMP2);
            voiceWarren1 = new Audio(AudioEnum.WARREN1.getFilePath(), false, AudioEnum.WARREN1);


        } catch (Exception e) {
            System.err.println("error playing audio" +e.getMessage());
            e.printStackTrace();
        }
    }


    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        } return instance;
    }

    // "uppgraderbart" fan heter det på engelska?
    public void playShot(int Index){
        switch(Index){
            case 1 -> audioShot1.play();
            case 2 -> audioShot2.play();
            default -> System.out.println("wrong index");
        }
    }

    public void playHit(String Type){
        switch(Type.toLowerCase()){
            case "small" -> audioHitSmall.play();
            case "medium" -> audioHitMedium.play();
            case "final" -> audioHitFinal.play();
            default -> System.out.println("wrong type");
        }
    }

    // random splash sound for misses
    public void playSplash(){
        int random = (int) (Math.random() * 4) +1;
        switch(random){
            case 1 -> audioSplash1.play();
            case 2 -> audioSplash2.play();
            case 3 -> audioSplash3.play();
            case 4 -> audioSplash4.play();
        }
    }

    public void playYesNo (String yesNo){
        switch(yesNo.toLowerCase()){
            case "yes" -> audioYes.play();
            case "no" -> audioNo.play();
            default -> System.out.println("wrong type");
        }
    }

    public void playClack(int Index){
        switch(Index){
            case 1 -> audioClack1.play();
            case 2 -> audioClack2.play();
            case 3 -> audioClack3.play();
            case 4 -> audioClack4.play();
            case 5 -> audioClack5.play();
            case 6 -> audioClack6.play();
            case 7 -> audioClack7.play();
            default -> System.out.println("wrong index");
        }
    }

    // controls for theme song
    public void playThemeSong(String playStopMute) {
        switch (playStopMute) {
            case "play" -> audioSongYachtTheme.play();
            case "stop" -> audioSongYachtTheme.stop();
            case "mute" -> audioSongYachtTheme.mute();
        }
    }

    public void playHit(AudioEnum hitType){
        switch(hitType) {
            case JP1 -> voiceJP1.play();
            case BEZ1 -> voiceBez1.play();
            case MUSK1 -> voiceMusk1.play();
            case TRUMP1 -> voiceTrump1.play();
            case WARREN1 -> voiceWarren1.play();
            default -> System.out.println("wrong type");
        }
    }

    // lowers volume on theme song by x decibels
    public void themeFadeDown(float targetVolumeDB){
        audioSongYachtTheme.setVolume(targetVolumeDB,20,60);
    }

    public void themeFadeUp(){
        audioSongYachtTheme.setVolume(0.0f,20,100);
    }
}
