package com.chengzhang.mupdfreader.app;

public interface CancellableTaskDefinition <Params, Result>
{
	public Result doInBackground(Params... params);
	public void doCancel();
	public void doCleanup();
}
