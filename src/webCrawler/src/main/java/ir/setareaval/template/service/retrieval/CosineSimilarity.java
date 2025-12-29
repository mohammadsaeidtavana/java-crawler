package ir.setareaval.template.service.retrieval;

import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class CosineSimilarity {
    public double cosine(Map<String, Double> q, Map<String, Double> d) {
        double dot = 0, qNorm = 0, dNorm = 0;
        for (String t : q.keySet()) {
            dot += q.get(t) * d.getOrDefault(t, 0.0);
            qNorm += q.get(t) * q.get(t);
        }
        for (double v : d.values())
            dNorm += v * v;

        return dot / (Math.sqrt(qNorm) * Math.sqrt(dNorm) + 1e-9);
    }
}
