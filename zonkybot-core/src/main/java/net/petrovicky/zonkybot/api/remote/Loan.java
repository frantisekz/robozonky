package net.petrovicky.zonkybot.api.remote;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class Loan {

    private boolean topped, covered, published;
    private int id, termInMonths, investmentsCount, questionsCount;
    private double amount, remainingInvestment;
    private String name, story, nickName;
    private BigDecimal interestRate;
    private Instant datePublished, deadline;
    private Rating rating;
    private Collection<Photo> photos;

    @XmlElement
    public int getId() {
        return id;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    @XmlElement
    public String getStory() {
        return story;
    }

    @XmlElement
    public String getNickName() {
        return nickName;
    }

    @XmlElement
    public int getTermInMonths() {
        return termInMonths;
    }

    @XmlElement
    public BigDecimal getInterestRate() {
        return interestRate;
    }

    @XmlElement
    @JsonDeserialize(using = RatingDeserializer.class)
    public Rating getRating() {
        return rating;
    }

    @XmlElement
    public boolean isTopped() {
        return topped;
    }

    @XmlElement
    public double getAmount() {
        return amount;
    }

    @XmlElement
    public double getRemainingInvestment() {
        return remainingInvestment;
    }

    @XmlElement
    public boolean isCovered() {
        return covered;
    }

    @XmlElement
    public boolean isPublished() {
        return published;
    }

    @XmlElement
    @JsonDeserialize(using = InstantDeserializer.class)
    public Instant getDatePublished() {
        return datePublished;
    }

    @XmlElement
    @JsonDeserialize(using = InstantDeserializer.class)
    public Instant getDeadline() {
        return deadline;
    }

    @XmlElement
    public int getInvestmentsCount() {
        return investmentsCount;
    }

    @XmlElement
    public int getQuestionsCount() {
        return questionsCount;
    }

    @XmlElement
    public Collection<Photo> getPhotos() {
        return photos;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Loan{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", nickName='").append(nickName).append('\'');
        sb.append(", termInMonths=").append(termInMonths);
        sb.append(", rating=").append(rating);
        sb.append(", remainingInvestment=").append(remainingInvestment);
        sb.append('}');
        return sb.toString();
    }
}
