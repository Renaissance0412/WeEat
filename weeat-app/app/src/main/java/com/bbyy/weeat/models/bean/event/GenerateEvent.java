package com.bbyy.weeat.models.bean.event;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
public class GenerateEvent {
    public boolean isGenerate() {
        return generate;
    }

    private boolean generate;

    public GenerateEvent(boolean generate) {
        this.generate = generate;
    }
}
