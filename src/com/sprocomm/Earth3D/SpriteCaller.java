package com.sprocomm.Earth3D;

public abstract class SpriteCaller implements SpriteCallerInterface {

	@Override
	abstract public void initData();

	@Override
	abstract public void visit();

	@Override
	abstract public void initDataHandler();

	@Override
	abstract public void openDataHandler();

}
