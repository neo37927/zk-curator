/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.main.base.pubsub;

import com.main.base.pubsub.messages.LocationAvailable;
import com.main.base.pubsub.messages.UserCreated;
import com.main.base.pubsub.models.Group;
import com.main.base.pubsub.models.Instance;
import com.main.base.pubsub.models.InstanceType;
import com.main.base.pubsub.models.Priority;
import org.apache.curator.x.async.modeled.JacksonModelSerializer;
import org.apache.curator.x.async.modeled.ModelSpec;
import org.apache.curator.x.async.modeled.ModelSpecBuilder;
import org.apache.curator.x.async.modeled.ModeledFramework;
import org.apache.curator.x.async.modeled.typed.TypedModeledFramework;
import org.apache.curator.x.async.modeled.typed.TypedModeledFramework2;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.TimeUnit;

public class Clients {
    /**
     * A client template for LocationAvailable instances
     */
    public static final TypedModeledFramework2<LocationAvailable, Group, Priority> locationAvailableClient = TypedModeledFramework2.from(
            ModeledFramework.builder(),
            builder(LocationAvailable.class),
            "/crd/root/pubsub/messages/locations/{group}/{priority}/{id}"
    );

    /**
     * A client template for UserCreated instances
     */
    public static final TypedModeledFramework2<UserCreated, Group, Priority> userCreatedClient = TypedModeledFramework2.from(
            ModeledFramework.builder(),
            builder(UserCreated.class),
            "/crd/root/pubsub/messages/users/{group}/{priority}/{id}"
    );

    /**
     * A client template for Instance instances
     */
    public static final TypedModeledFramework<Instance, InstanceType> instanceClient = TypedModeledFramework.from(
            ModeledFramework.builder(),
            builder(Instance.class),
            "/crd/root/pubsub/instances/{instance-type}/{id}"
    );

    private static <T> ModelSpecBuilder<T> builder(Class<T> clazz) {
        return ModelSpec.builder(JacksonModelSerializer.build(clazz))
//                .withTtl(TimeUnit.MINUTES.toMillis(10)) // for our pub-sub example, messages are valid for 10 minutes
                //TODO 支持3.5.*以上版本
//                .withCreateMode(CreateMode.PERSISTENT_WITH_TTL)
                .withCreateMode(CreateMode.EPHEMERAL)
                ;
    }

    private Clients() {
    }
}
