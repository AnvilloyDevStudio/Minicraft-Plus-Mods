package minicraftmodsapiinterface;

public interface IPotionType {
    public boolean toggleEffect(IPlayer player, boolean addEffect);
	
	public boolean transmitEffect();

}
