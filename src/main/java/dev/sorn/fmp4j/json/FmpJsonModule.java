package dev.sorn.fmp4j.json;

import com.fasterxml.jackson.databind.module.SimpleModule;
import dev.sorn.fmp4j.types.FmpCik;
import dev.sorn.fmp4j.types.FmpIsin;
import dev.sorn.fmp4j.types.FmpSymbol;

public final class FmpJsonModule extends SimpleModule {

    public static final String FMP_JSON_MODULE_NAME = "FmpJsonModule";

    public FmpJsonModule() {
        super(FMP_JSON_MODULE_NAME);
        this.addDeserializer(FmpCik.class, new FmpJsonBlankAsNullStringDeserializer<>(FmpCik::cik));
        this.addDeserializer(FmpIsin.class, new FmpJsonBlankAsNullStringDeserializer<>(FmpIsin::isin));
        this.addDeserializer(FmpSymbol.class, new FmpSymbolDeserializer());
    }
}
