package ru.pearx.libmc.client.models;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.pearx.libmc.PXLMC;
import ru.pearx.libmc.common.blocks.controllers.ConnectionsController;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrAppleXZ on 14.05.17 22:10.
 */
@SideOnly(Side.CLIENT)
public class ConnectedModel extends OvModel
{
    private ResourceLocation baseTexture;

    public ConnectedModel()
    {
        setShouldFlipV(false);
        setBaseModel(new ResourceLocation(PXLMC.MODID, "block/cube_all_colored"));
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
    {

        List<BakedQuad> quads = new ArrayList<>();
        for(BakedQuad baseQ : getBaked().getQuads(state, side, rand))
        {
            String digits = "0000";
            if(state instanceof IExtendedBlockState)
            {
                IExtendedBlockState ext = (IExtendedBlockState) state;
                switch (baseQ.getFace())
                {
                    case SOUTH:
                        digits = getConnectionsString(EnumFacing.UP, EnumFacing.EAST, EnumFacing.DOWN, EnumFacing.WEST, ext);
                        break;
                    case NORTH:
                        digits = getConnectionsString(EnumFacing.UP, EnumFacing.WEST, EnumFacing.DOWN, EnumFacing.EAST, ext);
                        break;
                    case UP:
                        digits = getConnectionsString(EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST, ext);
                        break;
                    case DOWN:
                        digits = getConnectionsString(EnumFacing.SOUTH, EnumFacing.EAST, EnumFacing.NORTH, EnumFacing.WEST, ext);
                        break;
                    case EAST:
                        digits = getConnectionsString(EnumFacing.UP, EnumFacing.NORTH, EnumFacing.DOWN, EnumFacing.SOUTH, ext);
                        break;
                    case WEST:
                        digits = getConnectionsString(EnumFacing.UP, EnumFacing.SOUTH, EnumFacing.DOWN, EnumFacing.NORTH, ext);
                        break;
                }
            }
            quads.add(new BakedQuadWNT(baseQ, getSprite(digits)));
        }
        process(quads, state, side, rand);
        return quads;
    }

    @Override
    public TextureAtlasSprite getParticleTexture()
    {
        return getSprite("0000");
    }

    public String getConnectionsString(EnumFacing north, EnumFacing east, EnumFacing south, EnumFacing west, IExtendedBlockState state)
    {
        try
        {
            return boolToShortString(state.getValue(ConnectionsController.PROPS.get(north))) + boolToShortString(state.getValue(ConnectionsController.PROPS.get(east))) + boolToShortString(state.getValue(ConnectionsController.PROPS.get(south))) + boolToShortString(state.getValue(ConnectionsController.PROPS.get(west)));
        }
        catch(Exception e)
        {
            return "0000";
        }
    }

    public String boolToShortString(Boolean bool)
    {
        return bool ? "1" : "0";
    }

    public TextureAtlasSprite getSprite(String digits)
    {
        return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(getBaseTexture().toString() + "/" + digits);
    }

    public ResourceLocation getBaseTexture()
    {
        return baseTexture;
    }

    public void setBaseTexture(ResourceLocation baseTexture)
    {
        this.baseTexture = baseTexture;
    }
}
