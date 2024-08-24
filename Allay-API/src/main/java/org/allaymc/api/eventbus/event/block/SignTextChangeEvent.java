package org.allaymc.api.eventbus.event.block;

import lombok.Getter;
import org.allaymc.api.block.BlockStateWithPos;
import org.allaymc.api.entity.interfaces.EntityPlayer;
import org.allaymc.api.eventbus.event.CancellableEvent;

/**
 * Allay Project 2024/8/16
 *
 * @author daoge_cmd
 */
@Getter
public class SignTextChangeEvent extends BlockEvent implements CancellableEvent {
    protected String[] text;
    protected EntityPlayer editor;

    public SignTextChangeEvent(BlockStateWithPos block, String[] text, EntityPlayer editor) {
        super(block);
        setText(text);
        this.editor = editor;
    }

    public void setText(String[] text) {
        if (text.length > 4) {
            throw new IllegalArgumentException("Sign text must be 4 lines or less");
        }
        this.text = text;
    }
}
