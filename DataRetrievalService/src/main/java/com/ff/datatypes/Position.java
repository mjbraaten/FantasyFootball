package com.ff.datatypes;

public enum Position {
	   QB("QB"),
	   RB("RB"),
	   WR("WR"),
	   TE("TE"),
	   K("K"),
	   DST("DST");

	    private final String value;

	    private Position(final String value) {
	        this.value = value;
	    }

	    public String getValue() {
	        return value;
	    }

	    @Override
	    public String toString() {
	        return getValue();
	    }
}
