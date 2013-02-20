package com.myuniver.intelligentsearch.questionanalyzer;

public class QuestionAnalyzed {
    /**
     * Question string.
     */
    private String question;
    /**
     * Normalized question string.
     */
    private String qn;
    /**
     * Question string with stemmed verbs and nouns.
     */
    private String stemmed;
    /**
     * Question string with modified verbs.
     */
    private String verbMod;
    /**
     * Keywords in the question and context.
     */
    private String[] kws;
    /**
     * Named entities in the question and context.
     */
    private String[][] nes;
    /**
     * Focus word.
     */
    private String focus;
    /**
     * Expected answer types.
     */
    private String[] ats = new String[0];
    /**
     * Interpretations of the question.
     */
    private QuestionInterpretation[] qis = new QuestionInterpretation[0];
    /**
     * Indicates that this is a factoid question.
     */
    private boolean isFactoid = true;

    public QuestionAnalyzed(String question) {
        setQuestion(question);
    }

    public QuestionAnalyzed(String question, String qn, String stemmed,
                            String verbMod, String[] kws, String[][] nes) {
        this(question);

        setNormalized(qn);
        setStemmed(stemmed);
        setVerbMod(verbMod);
        setKeywords(kws);
        setNes(nes);
    }

    public QuestionAnalyzed(String question, String qn, String stemmed,
                            String verbMod, String[] kws, String[][] nes, String focus, String[] ats, QuestionInterpretation[] qis) {
        this(question, qn, stemmed, verbMod, kws, nes);

        setFocus(focus);
        setAnswerTypes(ats);
        setInterpretations(qis);
    }

    public String getQuestion() {
        return question;
    }

    public String getNormalized() {
        return qn;
    }

    public String getStemmed() {
        return stemmed;
    }

    public String getVerbMod() {
        return verbMod;
    }

    public String[] getKeywords() {
        return kws;
    }

    public String[][] getNes() {
        return nes;
    }

    public String getFocus() {
        return focus;
    }

    public String[] getAnswerTypes() {
        return ats;
    }

    public QuestionInterpretation[] getInterpretations() {
        return qis;
    }

    public boolean isFactoid() {
        return isFactoid;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setNormalized(String qn) {
        this.qn = qn;
    }

    public void setStemmed(String stemmed) {
        this.stemmed = stemmed;
    }

    public void setVerbMod(String verbMod) {
        this.verbMod = verbMod;
    }

    public void setKeywords(String[] kws) {
        this.kws = kws;
    }

    public void setNes(String[][] nes) {
        this.nes = nes;
    }

    public void setFocus(String focus) {
        this.focus = focus;
    }

    public void setAnswerTypes(String[] ats) {
        this.ats = ats;
    }

    public void setInterpretations(QuestionInterpretation[] qis) {
        this.qis = qis;
    }

    public void setFactoid(boolean isFactoid) {
        this.isFactoid = isFactoid;
    }
}
