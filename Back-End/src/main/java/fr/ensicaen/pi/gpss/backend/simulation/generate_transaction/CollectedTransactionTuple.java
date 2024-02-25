package fr.ensicaen.pi.gpss.backend.simulation.generate_transaction;

import fr.ensicaen.pi.gpss.backend.payload.request.CollectedTransaction;

public record CollectedTransactionTuple(CollectedTransaction debit, CollectedTransaction credit) {
}
