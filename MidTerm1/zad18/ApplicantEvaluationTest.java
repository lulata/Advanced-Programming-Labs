package NP.MidTerm1.zad18;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


/**
 * I partial exam 2016
 */
public class ApplicantEvaluationTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        int creditScore = scanner.nextInt();
        int employmentYears = scanner.nextInt();
        boolean hasCriminalRecord = scanner.nextBoolean();
        int choice = scanner.nextInt();
        Applicant applicant = new Applicant(name, creditScore, employmentYears, hasCriminalRecord);
        Evaluator.TYPE type = Evaluator.TYPE.values()[choice];
        Evaluator evaluator = null;
        try {
            evaluator = EvaluatorBuilder.build(type);
            System.out.println("Applicant");
            System.out.println(applicant);
            System.out.println("Evaluation type: " + type.name());
            if (evaluator.evaluate(applicant)) {
                System.out.println("Applicant is ACCEPTED");
            } else {
                System.out.println("Applicant is REJECTED");
            }
        } catch (InvalidEvaluation invalidEvaluation) {
            System.out.println("Invalid evaluation");
        }
    }
}

class Applicant {
    private String name;

    private int creditScore;
    private int employmentYears;
    private boolean hasCriminalRecord;

    public Applicant(String name, int creditScore, int employmentYears, boolean hasCriminalRecord) {
        this.name = name;
        this.creditScore = creditScore;
        this.employmentYears = employmentYears;
        this.hasCriminalRecord = hasCriminalRecord;
    }

    public String getName() {
        return name;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public int getEmploymentYears() {
        return employmentYears;
    }

    public boolean hasCriminalRecord() {
        return hasCriminalRecord;
    }

    @Override
    public String toString() {
        return String.format("Name: %s\nCredit score: %d\nExperience: %d\nCriminal record: %s\n",
                name, creditScore, employmentYears, hasCriminalRecord ? "Yes" : "No");
    }
}

interface Evaluator {
    enum TYPE {
        NO_CRIMINAL_RECORD,
        MORE_EXPERIENCE,
        MORE_CREDIT_SCORE,
        NO_CRIMINAL_RECORD_AND_MORE_EXPERIENCE,
        MORE_EXPERIENCE_AND_MORE_CREDIT_SCORE,
        NO_CRIMINAL_RECORD_AND_MORE_CREDIT_SCORE,
        INVALID // should throw exception
    }

    boolean evaluate(Applicant applicant);
}

class InvalidEvaluation extends Exception {

}

class EvaluatorBuilder {
    public static Evaluator build(Evaluator.TYPE type) throws InvalidEvaluation {
        switch (type) {
            case INVALID:
                throw new InvalidEvaluation();
            case MORE_EXPERIENCE:
                return new EvaluatorDecorator(new EvaluatorExperience());
            case MORE_CREDIT_SCORE:
                return new EvaluatorDecorator(new EvaluatorCredit());
            case NO_CRIMINAL_RECORD:
                return new EvaluatorDecorator(new EvaluatorCriminal());
            case MORE_EXPERIENCE_AND_MORE_CREDIT_SCORE:
                return new EvaluatorDecorator(new EvaluatorExperience(), new EvaluatorCredit());
            case NO_CRIMINAL_RECORD_AND_MORE_EXPERIENCE:
                return new EvaluatorDecorator(new EvaluatorCriminal(), new EvaluatorExperience());
            case NO_CRIMINAL_RECORD_AND_MORE_CREDIT_SCORE:
                return new EvaluatorDecorator(new EvaluatorCriminal(), new EvaluatorCredit());
            default:
                throw new InvalidEvaluation();
        }
    }
}

class EvaluatorExperience implements Evaluator {

    @Override
    public boolean evaluate(Applicant applicant) {
        if (applicant.getEmploymentYears() < 10) {
            return false;
        }
        return true;
    }
}

class EvaluatorCredit implements Evaluator {

    @Override
    public boolean evaluate(Applicant applicant) {
        if (applicant.getCreditScore() < 500) {
            return false;
        }
        return true;
    }
}

class EvaluatorCriminal implements Evaluator {

    @Override
    public boolean evaluate(Applicant applicant) {
        if (applicant.hasCriminalRecord()) {
            return false;
        }
        return true;
    }
}

class EvaluatorDecorator implements Evaluator {
    List<Evaluator> evaluators;

    public EvaluatorDecorator(Evaluator... evaluators) {
        this.evaluators = new ArrayList<>(Arrays.asList(evaluators));

        /* if Experience and Criminal are sent */


    }

    @Override
    public boolean evaluate(Applicant applicant) {
        for (Evaluator e : evaluators) {
            if (e.evaluate(applicant) == false)
                return false;
        }
        return true;
    }
}
