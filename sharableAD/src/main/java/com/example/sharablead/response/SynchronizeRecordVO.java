package com.example.sharablead.response;

import com.example.sharablead.entity.SynchronizeRecord;
import lombok.Data;

@Data
public class SynchronizeRecordVO extends SynchronizeRecord {

    private String synchronizeTypeName;
}
