/*
 * Copyright 2012-2013 Continuuity,Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.continuuity.weave.internal.json;

import com.continuuity.weave.api.ResourceReport;
import com.continuuity.weave.api.WeaveRunResources;
import com.continuuity.weave.internal.DefaultResourceReport;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Codec for serializing and deserializing a {@link ResourceReport} object using json.
 */
public final class ResourceReportCodec implements JsonSerializer<ResourceReport>,
                                           JsonDeserializer<ResourceReport> {

  @Override
  public JsonElement serialize(ResourceReport src, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject json = new JsonObject();

    json.addProperty("appMasterId", src.getApplicationId());
    json.add("appMasterResources", context.serialize(
      src.getAppMasterResources(), new TypeToken<WeaveRunResources>(){}.getType()));
    json.add("runnableResources", context.serialize(
      src.getResources(), new TypeToken<Map<String, Collection<WeaveRunResources>>>(){}.getType()));

    return json;
  }

  @Override
  public ResourceReport deserialize(JsonElement json, Type typeOfT,
                                           JsonDeserializationContext context) throws JsonParseException {
    JsonObject jsonObj = json.getAsJsonObject();
    String appMasterId = jsonObj.get("appMasterId").getAsString();
    WeaveRunResources masterResources = context.deserialize(
      jsonObj.get("appMasterResources"), WeaveRunResources.class);
    Map<String, Collection<WeaveRunResources >> resources = context.deserialize(
      jsonObj.get("runnableResources"), new TypeToken<Map<String, Collection<WeaveRunResources>>>(){}.getType());

    return new DefaultResourceReport(appMasterId, masterResources, resources);
  }
}
