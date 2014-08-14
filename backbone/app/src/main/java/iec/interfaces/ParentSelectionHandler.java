package iec.interfaces;

import java.util.List;

import eplex.win.winBackbone.Artifact;

/**
 * Created by paul on 8/14/14.
 */
public interface ParentSelectionHandler
{
    void parentSelected(Artifact p);
    void parentUnselected(Artifact p);
    void currentParents(List<Artifact> parents);
}
