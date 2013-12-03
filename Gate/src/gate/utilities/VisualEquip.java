package gate.utilities;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.io.PrintStream;

public class VisualEquip
{
  public boolean visible = true;
  public static final int width = 32;
  public static final int height = 36;
  public static final int paddingX = 13;
  public static final int paddingY = 4;
  public Texture sheet;
  public TextureRegion region;
  private int newX;
  private int newY;
  private int newWidth;
  private AssetManager assets;
  public boolean showHair = false;
  public boolean forceBelow = false;
  public boolean flash = false;
  public float flashTime = 0.0F;
  public float curFlash = 0.0F;
  public float[] targetRGBA = { 1.0F, 1.0F, 1.0F, 1.0F };
  public float[] currentRGBA = { 1.0F, 1.0F, 1.0F, 1.0F };
  
  public VisualEquip(String filename, AssetManager assets)
  {
    this.assets = assets;
    if ((filename != null) && (Gdx.files.internal(filename).exists()))
    {
      this.sheet = ((Texture)assets.get(filename, Texture.class));
      this.visible = true;
    }
    else
    {
      this.sheet = ((Texture)assets.get("sprites/equips/empty.png", Texture.class));
      this.visible = false;
    }
    this.region = new TextureRegion(this.sheet, 0, 0, 0, 0);
    if (filename.equals("sprites/equips/helmet/9.png")) {
      this.showHair = true;
    }
  }
  
  public void changeTexture(String filename)
  {
    if (Gdx.files.internal(filename).exists())
    {
      this.sheet = ((Texture)this.assets.get(filename, Texture.class));
      if (filename.equals("sprites/equips/empty.png")) {
        this.visible = false;
      } else {
        this.visible = true;
      }
    }
    else
    {
      System.out.println("texture not found: " + filename);
    }
    this.region = new TextureRegion(this.sheet, 0, 0, 0, 0);
  }
  
  public void render(SpriteBatch batch, float x, float y, boolean flipX)
  {
    if ((this.visible) && (this.region != null))
    {
      if (this.flash) {
        flashPrecheck(batch);
      }
      if (!flipX) {
        batch.draw(this.region, x - 13.0F + 6.0F, y - 4.0F, this.newWidth, 36.0F);
      } else if (this.newWidth > 32) {
        batch.draw(this.region, x + 32.0F - 13.0F + 2.0F, y - 4.0F, -64.0F, 36.0F);
      } else {
        batch.draw(this.region, x + 32.0F - 13.0F + 2.0F, y - 4.0F, -32.0F, 36.0F);
      }
      if (this.flash) {
        flashPostcheck(batch);
      }
    }
  }
  
  public void update(TextureRegion region)
  {
    if (region != null)
    {
      this.newX = (region.getRegionX() / 14 * 32);
      if ((region.getRegionX() > 0) && (region.getRegionY() > 0)) {
        this.newX += 5;
      }
      this.newY = (region.getRegionY() / region.getRegionHeight() * 36);
      
      this.newWidth = (region.getRegionWidth() / 14 * 32);
      
      this.region.setRegion(this.newX, this.newY, this.newWidth, 36);
    }
  }
  
  public void flash(float r, float g, float b, float a, float duration)
  {
    this.curFlash = 0.0F;
    this.flashTime = duration;
    this.targetRGBA[0] = r;
    this.targetRGBA[1] = g;
    this.targetRGBA[2] = b;
    this.targetRGBA[3] = a;
    this.currentRGBA[0] = r;
    this.currentRGBA[1] = g;
    this.currentRGBA[2] = b;
    this.currentRGBA[3] = a;
    this.flash = true;
  }
  
  public void flashPrecheck(SpriteBatch batch)
  {
    for (int i = 0; i < this.currentRGBA.length; i++)
    {
      this.currentRGBA[i] += this.curFlash / this.flashTime * (1.0F - this.targetRGBA[i]);
      if (this.currentRGBA[i] > 1.0F) {
        this.currentRGBA[i] = 1.0F;
      }
    }
    this.curFlash += Gdx.graphics.getDeltaTime();
    batch.flush();
    batch.setColor(this.currentRGBA[0], this.currentRGBA[1], this.currentRGBA[2], this.currentRGBA[3]);
  }
  
  public void flashPostcheck(SpriteBatch batch)
  {
    batch.flush();
    batch.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    if (this.curFlash >= 0.6F)
    {
      this.flash = false;
      this.curFlash = 0.0F;
    }
  }
}
