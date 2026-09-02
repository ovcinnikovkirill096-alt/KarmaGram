package com.android.dx.dex.cf;

import com.android.dx.dex.code.DalvCode;
import com.android.dx.rop.code.RopMethod;
import java.io.PrintStream;

public final class CodeStatistics {
    private static final boolean DEBUG = false;
    public int runningDeltaRegisters = 0;
    public int runningDeltaInsns = 0;
    public int runningTotalInsns = 0;
    public int dexRunningDeltaRegisters = 0;
    public int dexRunningDeltaInsns = 0;
    public int dexRunningTotalInsns = 0;
    public int runningOriginalBytes = 0;

    public void updateOriginalByteCount(int i) {
        this.runningOriginalBytes += i;
    }

    public void updateDexStatistics(DalvCode dalvCode, DalvCode dalvCode2) {
        this.dexRunningDeltaInsns += dalvCode2.getInsns().codeSize() - dalvCode.getInsns().codeSize();
        this.dexRunningDeltaRegisters += dalvCode2.getInsns().getRegistersSize() - dalvCode.getInsns().getRegistersSize();
        this.dexRunningTotalInsns += dalvCode2.getInsns().codeSize();
    }

    public void updateRopStatistics(RopMethod ropMethod, RopMethod ropMethod2) {
        int effectiveInstructionCount = ropMethod.getBlocks().getEffectiveInstructionCount();
        int regCount = ropMethod.getBlocks().getRegCount();
        int effectiveInstructionCount2 = ropMethod2.getBlocks().getEffectiveInstructionCount();
        this.runningDeltaInsns += effectiveInstructionCount2 - effectiveInstructionCount;
        this.runningDeltaRegisters += ropMethod2.getBlocks().getRegCount() - regCount;
        this.runningTotalInsns += effectiveInstructionCount2;
    }

    public void dumpStatistics(PrintStream printStream) {
        Integer numValueOf = Integer.valueOf(this.runningDeltaInsns);
        Integer numValueOf2 = Integer.valueOf(this.runningTotalInsns);
        int i = this.runningDeltaInsns;
        printStream.printf("Optimizer Delta Rop Insns: %d total: %d (%.2f%%) Delta Registers: %d\n", numValueOf, numValueOf2, Double.valueOf(((double) (i / (this.runningTotalInsns + Math.abs(i)))) * 100.0d), Integer.valueOf(this.runningDeltaRegisters));
        Integer numValueOf3 = Integer.valueOf(this.dexRunningDeltaInsns);
        Integer numValueOf4 = Integer.valueOf(this.dexRunningTotalInsns);
        int i2 = this.dexRunningDeltaInsns;
        printStream.printf("Optimizer Delta Dex Insns: Insns: %d total: %d (%.2f%%) Delta Registers: %d\n", numValueOf3, numValueOf4, Double.valueOf(((double) (i2 / (this.dexRunningTotalInsns + Math.abs(i2)))) * 100.0d), Integer.valueOf(this.dexRunningDeltaRegisters));
        printStream.printf("Original bytecode byte count: %d\n", Integer.valueOf(this.runningOriginalBytes));
    }
}
