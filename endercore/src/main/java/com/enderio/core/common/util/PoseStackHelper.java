package com.enderio.core.common.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

public class PoseStackHelper {
	
	public static void rotateAroundPivot(PoseStack poseStack, Vector3f pivot, Vector3f axis, float angle, boolean degrees){
		poseStack.translate(pivot.x(), pivot.y(), pivot.z());
		poseStack.mulPose(new Quaternion(axis, angle, degrees));
		poseStack.translate(-pivot.x(), -pivot.y(), -pivot.z());
	}

}
