package com.chengzhang.mupdfreader.app.ui;

public class PassClickResultSignature extends PassClickResult {
    public final SignatureState state;

    public PassClickResultSignature(boolean _changed, int _state) {
        super(_changed);
        state = SignatureState.values()[_state];
    }

    public void acceptVisitor(PassClickResultVisitor visitor) {
        visitor.visitSignature(this);
    }
}
