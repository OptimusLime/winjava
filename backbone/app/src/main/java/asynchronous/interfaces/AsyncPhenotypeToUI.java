package asynchronous.interfaces;

import android.app.Activity;

import bolts.Task;

/**
 * Created by paul on 8/14/14.
 */
public interface AsyncPhenotypeToUI<PhenoClass, UIClass> {

    Task<UIClass> asyncPhenotypeToUI(Activity act, PhenoClass phenotype);
}
