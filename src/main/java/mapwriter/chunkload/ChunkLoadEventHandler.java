package mapwriter.chunkload;

import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.HashMap;

public class ChunkLoadEventHandler {

	public final HashMap<String, OverlayChunk> chunks = new HashMap<String, OverlayChunk>();

	@SubscribeEvent
	public void eventJoinWorld(FMLNetworkEvent.ClientConnectedToServerEvent event) {
		synchronized (chunks) {
			chunks.clear();
		}
	}

	@SubscribeEvent
	public void eventChunkLoad(ChunkEvent.Load event) {
		Chunk chunk = event.getChunk();
		OverlayChunk oc = new OverlayChunk(chunk.xPosition, chunk.zPosition);
		synchronized (chunks) {
			chunks.put(oc.getKey(), oc);
		}
	}

	@SubscribeEvent
	public void eventChunkUnload(ChunkEvent.Unload event) {
		Chunk chunk = event.getChunk();
		String posLong = OverlayChunk.getKey(chunk.xPosition, chunk.zPosition);
		synchronized (chunks) {
			OverlayChunk oc = chunks.get(posLong);
			if (oc == null) {
				oc = new OverlayChunk(chunk.xPosition, chunk.zPosition);
				chunks.put(posLong, oc);
			}
			oc.unload();
		}
	}
}
