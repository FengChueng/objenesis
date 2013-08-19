/**
 * Copyright 2006-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.objenesis.instantiator.android;

import java.io.ObjectInputStream;
import java.lang.reflect.Method;

import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;

/**
 * Instantiator for Android <= 2.3 which creates objects without driving their constructors, using internal
 * methods on the Dalvik implementation of {@link java.io.ObjectInputStream}.
 *
 * @author Piotr 'Qertoip' Włodarek
 */
public class Android23Instantiator<T> implements ObjectInstantiator<T> {
   private final Class<T> type;
   private final Method newStaticMethod;

   public Android23Instantiator(Class<T> type) {
      this.type = type;
      newStaticMethod = getNewStaticMethod();
   }

   @SuppressWarnings("unchecked")
   public T newInstance() {
      try {
         return (T) newStaticMethod.invoke(null, type, Object.class);
      }
      catch(Exception e) {
         throw new ObjenesisException(e);
      }
   }

   private static Method getNewStaticMethod() {
      try {
         Method newStaticMethod = ObjectInputStream.class.getDeclaredMethod(
           "newInstance", Class.class, Class.class);
         newStaticMethod.setAccessible(true);
         return newStaticMethod;
      }
      catch(RuntimeException e) {
         throw new ObjenesisException(e);
      }
      catch(NoSuchMethodException e) {
         throw new ObjenesisException(e);
      }
   }

}