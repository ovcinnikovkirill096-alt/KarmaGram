package com.sun.jna;

public interface SymbolProvider {
    long getSymbolAddress(long j, String str, SymbolProvider symbolProvider);
}
