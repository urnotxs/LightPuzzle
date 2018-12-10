package com.xs.lightpuzzle.puzzle.exchanged.observer;

import com.xs.lightpuzzle.puzzle.info.low.PuzzleImagePieceInfo;

/**
 * Created by xs on 2018/11/30.
 */

public interface IObserver {
    public void updateHandlingPiece(PuzzleImagePieceInfo info);
}
