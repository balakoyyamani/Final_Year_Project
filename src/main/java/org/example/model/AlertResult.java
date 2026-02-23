package org.example.model;

public class AlertResult {

    private double score;
    private String riskType;
    private String level;

    public AlertResult(double score, String riskType, String level) {
        this.score = score;
        this.riskType = riskType;
        this.level = level;
    }

    public double getScore() { return score; }
    public String getRiskType() { return riskType; }
    public String getLevel() { return level; }
}