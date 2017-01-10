package com.example.rodrigo.auction.repository.database;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

public interface UserColumns {

    @DataType(INTEGER)
    @PrimaryKey
    @AutoIncrement
    String ID = "_id";

    @DataType(TEXT)
    @NotNull
    String NAME = "user_userName";
}
