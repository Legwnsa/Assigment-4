package com.example.edit;

abstract class CountedEditDistance implements EditDistance {
    protected long ops;
    protected long memoryCells;

    protected void resetCounters() {
        ops = 0;
        memoryCells = 0;
    }

    @Override
    public long getOps() {
        return ops;
    }

    @Override
    public long getMemoryCells() {
        return memoryCells;
    }
}
