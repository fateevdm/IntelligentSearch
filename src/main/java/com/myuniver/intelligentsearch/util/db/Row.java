package com.myuniver.intelligentsearch.util.db;

/**
 * User: Dmitry Fateev
 * Date: 21.05.13
 * Time: 19:19
 * email: wearing.fateev@gmail.com
 */
public class Row {
    private final String originText;
    private final String fact;
    private final int questionId;
    private final int answerId;

    public static final Row BAD_OBJECT = Builder.start().setOriginText("error in time constructing").build();

    private Row(Builder builder) {
        originText = builder.originText;
        answerId = builder.answerId;
        questionId = builder.questionId;
        fact = builder.fact;
    }


    public String getOriginText() {
        return originText;
    }

    public String getFact() {
        return fact;
    }

    public int getQuestionId() {
        return questionId;
    }

    public int getAnswerId() {
        return answerId;
    }

    public static class Builder {
        String originText;
        String fact;
        int questionId;
        int answerId;

        public static Builder start() {
            return new Builder();
        }

        public Builder setOriginText(String originText) {
            this.originText = originText;
            return this;
        }

        public Builder setFact(String fact) {
            this.fact = fact;
            return this;
        }

        public Builder setQuestionId(int questionId) {
            this.questionId = questionId;
            return this;
        }

        public Builder setAnswerId(int answerId) {
            this.answerId = answerId;
            return this;
        }

        public Row build() {
            return new Row(this);
        }
    }

}
