package net.zaharenko424.casualties_cubed.util;

public record Keyframe(float time, float value, float inTangent, float outTangent, WeightedMode weightedMode, float inWeight, float outWeight) {}
