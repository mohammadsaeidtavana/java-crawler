package ir.setareaval.template.service.retrieval;

import org.springframework.stereotype.Service;

@Service
public class TfIdfCalculator {

    public double tf(int freq) {
        return freq;
    }

    public double idf(int N, int df) {
        return Math.log((double)(N + 1) / (df + 1)) + 1;
    }
}
