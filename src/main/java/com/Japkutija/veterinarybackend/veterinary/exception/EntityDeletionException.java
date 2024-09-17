package com.Japkutija.veterinarybackend.veterinary.exception;

import com.Japkutija.veterinarybackend.veterinary.model.entity.Breed;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Pet;

public class EntityDeletionException extends RuntimeException {

    public EntityDeletionException(Class<?> petClass, String errorOccurredWhileDeletingPet, Exception ex) {

    }
}
