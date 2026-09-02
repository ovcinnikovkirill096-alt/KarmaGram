package com.exteragram.messenger.api.db;

import com.exteragram.messenger.api.dto.AddedRegDateDTO;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

public interface AddedRegDateDao {
    Object insert(AddedRegDateDTO addedRegDateDTO, Continuation<? super Unit> continuation);

    Object isAdded(long j, Continuation<? super Boolean> continuation);
}
