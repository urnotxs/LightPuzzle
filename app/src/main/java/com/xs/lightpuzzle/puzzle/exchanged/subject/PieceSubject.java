package com.xs.lightpuzzle.puzzle.exchanged.subject;

import com.xs.lightpuzzle.puzzle.exchanged.observer.IObserver;
import com.xs.lightpuzzle.puzzle.info.low.PuzzleImagePieceInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xs on 2018/11/30.
 */

public class PieceSubject implements ISubject {

    private List<IObserver> observerList = new ArrayList<>();

    private PuzzleImagePieceInfo pieceInfo;

    @Override
    public void addObserver(IObserver observer) {
        observerList.add(observer);
    }

    @Override
    public void deleteObserver(IObserver observer) {
        if (observerList.contains(observer)) {
            observerList.remove(observer);
        }
    }

    @Override
    public void notifyObserver() {
        for (IObserver observer : observerList) {
            observer.updateHandlingPiece(pieceInfo);
            pieceInfo = null;
        }
    }

    public void publish(PuzzleImagePieceInfo pieceInfo) {
        this.pieceInfo = pieceInfo;
        notifyObserver();
    }
}
