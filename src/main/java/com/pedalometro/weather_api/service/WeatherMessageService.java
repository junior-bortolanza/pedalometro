package com.pedalometro.weather_api.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class WeatherMessageService {

    public String getMessage(Integer score) {
        if (score >= 80) {
            return getRandomMessage(getBestMessages());
        }
        if (score >= 60) {
            return getRandomMessage(getGoodMessages());
        }
        if (score >= 40) {
            return getRandomMessage(getRegularMessages());
        }
        return getRandomMessage(getBadMessages());
    }

    private String getRandomMessage(List<String> messages) {
        int random = ThreadLocalRandom.current().nextInt(messages.size());
        return messages.get(random);
    }

    private List<String> getBestMessages() {
        return List.of(
                "Bora, Fuzilos! Hoje até o Alemão pode sair sem medo de chuva.",
                "Hoje o Raide não tem desculpa pra querer voltar com 30 km.",
                "Fino, acorda! O pedal tá liberado.",
                "Mario, pode até ir de elétrica, mas hoje o clima tá ajudando.",
                "Craudinho, hoje a cobra verde tirou folga.",
                "Rulio, nenhuma onça confirmou presença no percurso.",
                "Gar, deixa as latinhas pra depois do pedal.",
                "Primo, hoje aguenta firme que o clima tá ajudando.",
                "Hoje ninguém inventa desculpa. Só bora pedalar!",
                "Até São Pedro resolveu colaborar com os Fuzilos hoje."
        );
    }

    private List<String> getGoodMessages() {
        return List.of(
                "Bora, Fuzilos! Hoje até o Alemão pode sair sem medo de chuva.",
                "Hoje o Raide não tem desculpa pra querer voltar com 30 km.",
                "Fino, acorda! O pedal tá liberado.",
                "Mario, pode até ir de elétrica, mas hoje o clima tá ajudando.",
                "Craudinho, hoje a cobra verde tirou folga.",
                "Rulio, nenhuma onça confirmou presença no percurso.",
                "Gar, deixa as latinhas pra depois do pedal.",
                "Primo, hoje aguenta firme que o clima tá ajudando.",
                "Hoje ninguém inventa desculpa. Só bora pedalar!",
                "Até São Pedro resolveu colaborar com os Fuzilos hoje."
        );
    }

    private List<String> getRegularMessages() {
        return List.of(
                "Alemão já começou a olhar a previsão de chuva de novo.",
                "Raide já perguntou se dá pra mudar o rolê antes de sair.",
                "Rulio ouviu um barulho no mato e já voltou pra casa.",
                "Craudinho viu uma mangueira e achou que era cobra verde.",
                "Gar já tá procurando onde comprar a primeira latinha.",
                "Fino falou que vai... depois do almoço.",
                "Primo vai precisar lembrar que ainda faltam muitos quilômetros.",
                "Mario perguntou se tem tomada no meio do caminho.",
                "Hoje o pedal é por sua conta e risco, Fuzilo.",
                "O clima tá meio suspeito... hoje até o grupo do WhatsApp vai ficar dividido."
        );
    }

    private List<String> getBadMessages() {
        return List.of(
                "Manga proibiu! Hoje é só resenha e cerveja.",
                "Alemão já cancelou por causa de uma nuvem no horizonte.",
                "Raide nem saiu de casa e já queria trocar o percurso.",
                "Rulio viu um cachorro e achou que era onça.",
                "Craudinho encontrou uma mangueira e pediu pra voltar.",
                "Gar já abriu a primeira latinha antes mesmo do horário.",
                "Fino mandou mensagem: 'me acorda no próximo pedal'.",
                "Mario colocou a elétrica pra carregar e foi dormir.",
                "Primo agradeceu o clima por dar uma desculpa pra descansar.",
                "Hoje até o Strava falou: melhor deixar pra amanhã."
        );
    }
}
