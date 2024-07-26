package fr.communaywen.core.corporation.guilds.data;

import java.util.UUID;

public record TransactionData(double value, String nature, String place, UUID sender) {

}
