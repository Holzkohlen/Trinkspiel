package lukashempel.trinkspielgedoenstestnumero2;

import java.io.InputStream;
import java.util.List;
import java.util.Random;

public class CGame {
    private CPlayer[] playerArray;
    private Object[] oFile;
    private Random rnd;
    private boolean bActiveRule;
    private int iLastRuleIndex;
    private String sLastRuleName;
    private int iQuestionActiveDuration;
    private boolean[] bPlayedAlready;
    private int iDebug;
    private int iDanielActive;
    private int iSwitchBelow; //Below this threshold a new rule triggers an old rule to be nullified
    //even if iQuestionActiveDuration isn't at 0 yet

    public CGame(String[] playerNames, InputStream inputStream) {

        iDanielActive = 0;
        playerArray = new CPlayer[playerNames.length];
        for (int i = 0; i < playerNames.length; i++) {
            playerArray[i] = new CPlayer(playerNames[i]);
            if(playerNames[i].equals("Daniel")){iDanielActive = i;}
        }
        CSVFile csvFile = new CSVFile(inputStream);
        List scoreList = csvFile.read();
        oFile = scoreList.toArray();
        rnd = new Random();
        bActiveRule = false;
        iLastRuleIndex = 0;
        iQuestionActiveDuration = 1;
        bPlayedAlready = new boolean[oFile.length];
        iDebug = oFile.length -1;
        iSwitchBelow = oFile.length / 3;
    }

    public String[] getQuestion() {
        String[] sReturnArray = new String[3];//0 => Header, 1 => Aufgabe, 2 => iDebug
        if (iDebug == 0) { //Wenn alle Fragen bereits gespielt sind
            sReturnArray[1] = "FINISHED";
            sReturnArray[0] = "Alle " + oFile.length + " Aufgaben gespielt!";
        } else {
            String[] sArray = new String[3];
            if (bActiveRule && iQuestionActiveDuration <= 0) { //wenn Regel aktiv && Zähler auf 0 => Regel aufheben
                sArray = (String[]) oFile[iLastRuleIndex];
                bActiveRule = false;
                sReturnArray[0] = wildcards(sArray[2], 2);
                sReturnArray[1] = "REGEL AUFGEHOBEN";
            } else { //Neue Aufgabe oder iDebug < 20
                int iIndex;
                boolean bAgain = true;
                do {
                    do {
                        iIndex = rnd.nextInt(bPlayedAlready.length);
                    } while (bPlayedAlready[iIndex]);
                    sArray = (String[])oFile[iIndex];
                    if ((!bActiveRule) || (!sArray[1].equals("1")) || iDebug < iSwitchBelow) {
                        bAgain = false;
                    }
                }while(bAgain);
                if(bActiveRule && sArray[1].equals("1")){ //iDebug has to be below iSwitchBelow!
                    sArray = (String[])oFile[iLastRuleIndex];
                    bActiveRule = false;
                    sReturnArray[0] = wildcards(sArray[2], 2);
                    sReturnArray[1] = "REGEL AUFGEHOBEN";
                }else {
                    bPlayedAlready[iIndex] = true;
                    iDebug--; //Neue Frage wird gespielt => Zähler--

                    if (bActiveRule && iQuestionActiveDuration > 0) {
                        iQuestionActiveDuration--;
                    }
                    sArray = (String[]) oFile[iIndex];
                    if (sArray[1].equals("1")) {
                        sReturnArray[1] = "NEUE REGEL";
                        iQuestionActiveDuration = rnd.nextInt(5) + 3;
                        iLastRuleIndex = iIndex;
                        bActiveRule = true;
                        sReturnArray[0] = wildcards(sArray[0], 1);
                    } else if (sArray[1].equals("2")) {
                        sReturnArray[1] = "GAME";
                        sReturnArray[0] = wildcards(sArray[0], 0);
                    } else {
                        sReturnArray[1] = "STANDARD";
                        sReturnArray[0] = wildcards(sArray[0], 0);
                    }
                }
            }
        }
        sReturnArray[2] = Integer.toString(iDebug);
        return sReturnArray;
    }

    private String wildcards(String sString, int iSave) {
        String sText = sString;
        Boolean bSkip = false;
        if(sString.contains("russischem Akzent")){
            bSkip = true;
            String[] parts = sText.split("%");
            String sName = playerArray[iDanielActive].getName();

            if (sText.indexOf("%") != 0) {
                sText = parts[0] + sName + parts[1];
            } else {
                sText = sName + parts[1];
            }
            if (iSave == 1) {
                sLastRuleName = sName;
            }
        }

        if (sString.contains("#")) { //Replace # with Random int for shot amount
            String[] parts = sText.split("#");
            int iShots = rnd.nextInt(5) + 1;
            if (parts.length != 1) {
                if (iShots == 1) {
                    sText = parts[0] + iShots + " Schluck" + parts[1];
                } else {
                    sText = parts[0] + iShots + " Schlücke" + parts[1];
                }
            } else {
                if (iShots == 1) {
                    sText = parts[0] + iShots + " Schluck";
                } else {
                    sText = parts[0] + iShots + " Schlücke";
                }
            }
        }

        if(!bSkip) {
            String sName = "";
            if (sString.contains("%")) { //Replace the name spaces with actual names
                String[] parts = sText.split("%");
                if (iSave == 2) {
                    sName = sLastRuleName;
                } else {
                    sName = playerArray[rnd.nextInt(playerArray.length)].getName();
                }
                if (sText.indexOf("%") != 0) {
                    sText = parts[0] + sName + parts[1];
                } else {
                    sText = sName + parts[1];
                }
                if (iSave == 1) {
                    sLastRuleName = sName;
                }
            }
            if (sString.contains("@")) {
                String[] parts = sText.split("@");
                String sName2 = playerArray[rnd.nextInt(playerArray.length)].getName();
                while (sName2.equals(sName)) {
                    sName2 = playerArray[rnd.nextInt(playerArray.length)].getName();
                }
                if (parts.length != 1) {
                    sText = parts[0] + sName2 + parts[1];
                } else {
                    sText = parts[0] + sName2;
                }
            }
        }
        return sText;
    }

    public int getPlayerArrayLength(){
        return playerArray.length;
    }
}
