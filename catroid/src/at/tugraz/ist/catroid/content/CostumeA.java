/**
 *  Catroid: An on-device graphical programming language for Android devices
 *  Copyright (C) 2010  Catroid development team 
 *  (<http://code.google.com/p/catroid/wiki/Credits>)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package at.tugraz.ist.catroid.content;

import java.util.concurrent.Semaphore;

import at.tugraz.ist.catroid.utils.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actors.Image;

/**
 * @author Johannes Iber
 * 
 */
public class CostumeA extends Image {
	private Semaphore xyLock = new Semaphore(1);
	private Semaphore imageLock = new Semaphore(1);
	private boolean imageChanged = false;
	private String imagePath;
	private Sprite sprite;
	public float alphaValue;

	public CostumeA(Sprite sprite) {
		super(Utils.getUniqueName());
		this.sprite = sprite;
		this.x = 0;
		this.y = 0;
		this.alphaValue = 1f;
		this.width = 0f;
		this.height = 0f;
		this.touchable = true;
	}

	@Override
	protected boolean touchDown(float x, float y, int pointer) {
		sprite.startTapScripts();
		return true;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		checkImageChanged();
		super.draw(batch, this.alphaValue);
	}

	private void checkImageChanged() {
		imageLock.acquireUninterruptibly();
		if (imageChanged) {
			if (this.region != null && this.region.getTexture() != null) {
				this.region.getTexture().dispose();
			}
			if (imagePath.equals("")) {
				this.x += this.width / 2;
				this.y += this.height / 2;
				this.width = 0f;
				this.height = 0f;
				imageChanged = false;
				imageLock.release();
				return;
			}
			//			TextureRegion textureRegion = TextureHandler.getInstance().getTexture(imagePath);
			//			if (textureRegion == null) {
			//				imageLock.release();
			//				return;
			//			}

			this.x += this.width / 2;
			this.y += this.height / 2;
			Texture tex = new Texture(Gdx.files.absolute(imagePath));
			TextureRegion textureRegion = new TextureRegion(tex);
			this.region = textureRegion;
			this.width = tex.getWidth();
			this.height = tex.getHeight();
			this.x -= this.width / 2;
			this.y -= this.height / 2;
			imageChanged = false;
		}
		imageLock.release();
	}

	public void setXPosition(float x) {
		xyLock.acquireUninterruptibly();
		if (this.region != null && this.region.getTexture() != null) {
			this.x = x - this.width / 2;
		} else {
			this.x = x;
		}
		xyLock.release();
	}

	public void setYPosition(float y) {
		xyLock.acquireUninterruptibly();
		if (this.region != null && this.region.getTexture() != null) {
			this.y = y - this.height / 2;
		} else {
			this.y = y;
		}
		xyLock.release();
	}

	public void setXYPosition(float x, float y) {
		xyLock.acquireUninterruptibly();
		if (this.region != null && this.region.getTexture() != null) {
			this.x = x - this.width / 2;
			this.y = y - this.height / 2;
		} else {
			this.x = x;
			this.y = y;
		}
		xyLock.release();
	}

	public float getXPosition() {
		float xPos = this.x;
		if (this.region != null && this.region.getTexture() != null) {
			xPos += this.width / 2;
		}
		return xPos;
	}

	public float getYPosition() {
		float yPos = this.y;
		if (this.region != null && this.region.getTexture() != null) {
			yPos += this.height / 2;
		}
		return yPos;
	}

	public void setImagePath(String path) {
		if (path == null) {
			path = "";
		}
		imageLock.acquireUninterruptibly();
		imagePath = path;
		imageChanged = true;
		imageLock.release();
	}
}
