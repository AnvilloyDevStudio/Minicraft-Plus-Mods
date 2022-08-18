package minicraft.mods.coremods.mixins;

import java.lang.reflect.Type;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

@Mixin(Gson.class)
public abstract class GsonMixin {
	@Shadow(remap = false)
	public abstract Object fromJson(String json, Type typeOfT) throws JsonSyntaxException;

	@Inject(at = @At(value = "HEAD", remap = false), method = "fromJson(Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;", remap = false, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void inject(String json, Class classOfT, CallbackInfoReturnable info) {
		try {
			Object object = fromJson(json, classOfT);
			System.out.println(object.getClass());
			System.out.println(classOfT);
			classOfT.cast(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
