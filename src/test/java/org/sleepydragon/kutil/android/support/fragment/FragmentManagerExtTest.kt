/*
 * Copyright (C) 2017 Denver Coneybeare <denver@sleepydragon.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sleepydragon.kutil.android.support.fragment

import android.annotation.SuppressLint
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import org.junit.Assert.assertSame
import org.junit.Test
import org.mockito.Mockito
import org.sleepydragon.kutil.android.FragmentCastException
import org.sleepydragon.kutil.android.FragmentNotFoundException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.fail

class FragmentManagerExtTest {

    @Test
    fun test_findFragmentByIdOrThrow() {
        class FoodFragment : Fragment()
        open class AnimalFragment : Fragment()
        class DogFragment : AnimalFragment()

        val id = 5

        run {
            val fm = Mockito.mock(FragmentManager::class.java)
            val fragment = DogFragment()
            Mockito.`when`(fm.findFragmentById(id)).thenReturn(fragment)
            assertSame(fragment, fm.findFragmentByIdOrThrow<DogFragment>(id))
        }
        run {
            val fm = Mockito.mock(FragmentManager::class.java)
            val fragment = DogFragment()
            Mockito.`when`(fm.findFragmentById(id)).thenReturn(fragment)
            assertSame(fragment, fm.findFragmentByIdOrThrow<AnimalFragment>(id))
        }
        run {
            val fm = Mockito.mock(FragmentManager::class.java)
            Mockito.`when`(fm.findFragmentById(id)).thenReturn(null)
            val exception = assertFailsWith<FragmentNotFoundException> { fm.findFragmentByIdOrThrow<Fragment>(id) }
            assertEquals("fragment with ID $id not found", exception.message)
        }
        run {
            val fm = Mockito.mock(FragmentManager::class.java)
            val fragment = FoodFragment()
            Mockito.`when`(fm.findFragmentById(id)).thenReturn(fragment)
            val exception = assertFailsWith<FragmentCastException> { fm.findFragmentByIdOrThrow<AnimalFragment>(id) }
            assertEquals("fragment with ID $id was found, but was an instance of ${fragment::class} "
                    + "when expecting an instance of ${AnimalFragment::class}", exception.message)
        }
    }

    @Test
    fun test_findFragmentByTagOrThrow() {
        class FoodFragment : Fragment()
        open class AnimalFragment : Fragment()
        class DogFragment : AnimalFragment()

        val tag = "tag"

        run {
            val fm = Mockito.mock(FragmentManager::class.java)
            val fragment = DogFragment()
            Mockito.`when`(fm.findFragmentByTag(tag)).thenReturn(fragment)
            assertSame(fragment, fm.findFragmentByTagOrThrow<DogFragment>(tag))
        }
        run {
            val fm = Mockito.mock(FragmentManager::class.java)
            val fragment = DogFragment()
            Mockito.`when`(fm.findFragmentByTag(tag)).thenReturn(fragment)
            assertSame(fragment, fm.findFragmentByTagOrThrow<AnimalFragment>(tag))
        }
        run {
            val fm = Mockito.mock(FragmentManager::class.java)
            Mockito.`when`(fm.findFragmentByTag(tag)).thenReturn(null)
            val exception = assertFailsWith<FragmentNotFoundException> { fm.findFragmentByTagOrThrow<Fragment>(tag) }
            assertEquals("fragment with tag $tag not found", exception.message)
        }
        run {
            val fm = Mockito.mock(FragmentManager::class.java)
            val fragment = FoodFragment()
            Mockito.`when`(fm.findFragmentByTag(tag)).thenReturn(fragment)
            val exception = assertFailsWith<FragmentCastException> { fm.findFragmentByTagOrThrow<AnimalFragment>(tag) }
            assertEquals("fragment with tag $tag was found, but was an instance of ${fragment::class} "
                    + "when expecting an instance of ${AnimalFragment::class}", exception.message)
        }
    }

    @SuppressLint("CommitTransaction")
    @Test
    fun test_findFragmentByIdOrAdd() {
        class FoodFragment : Fragment()
        class AnimalFragment : Fragment()

        val id = 5

        run {
            val fm = Mockito.mock(FragmentManager::class.java)
            val fragment = FoodFragment()
            Mockito.`when`(fm.findFragmentById(id)).thenReturn(fragment)
            assertSame(fragment, fm.findFragmentByIdOrAdd<FoodFragment>(id) { fail("should not be executed") })
        }
        run {
            val fm = Mockito.mock(FragmentManager::class.java)
            val ft = Mockito.mock(FragmentTransaction::class.java)
            val fragment = FoodFragment()
            Mockito.`when`(fm.findFragmentById(id)).thenReturn(null)
            Mockito.`when`(fm.beginTransaction()).thenReturn(ft)
            Mockito.`when`(ft.add(id, fragment)).thenReturn(ft)
            assertSame(fragment, fm.findFragmentByIdOrAdd<FoodFragment>(id) { fragment })
            Mockito.verify(fm).beginTransaction()
            Mockito.verify(ft).add(id, fragment)
            Mockito.verify(ft).commit()
        }
        run {
            val fm = Mockito.mock(FragmentManager::class.java)
            val fragment = FoodFragment()
            Mockito.`when`(fm.findFragmentById(id)).thenReturn(fragment)
            val exception = assertFailsWith<FragmentCastException> {
                fm.findFragmentByIdOrAdd<AnimalFragment>(id) { fail("should not be executed") }
            }
            assertEquals("fragment with ID $id was found, but was an instance of ${fragment::class} "
                    + "when expecting an instance of ${AnimalFragment::class}", exception.message)
        }
    }

    @SuppressLint("CommitTransaction")
    @Test
    fun test_findFragmentByIdOrReplace() {
        class FoodFragment : Fragment()
        class AnimalFragment : Fragment()

        val id = 5

        run {
            val fm = Mockito.mock(FragmentManager::class.java)
            val fragment = FoodFragment()
            Mockito.`when`(fm.findFragmentById(id)).thenReturn(fragment)
            assertSame(fragment, fm.findFragmentByIdOrReplace<FoodFragment>(id) { fail("should not be executed") })
        }
        run {
            val fm = Mockito.mock(FragmentManager::class.java)
            val ft = Mockito.mock(FragmentTransaction::class.java)
            val fragment = FoodFragment()
            Mockito.`when`(fm.findFragmentById(id)).thenReturn(null)
            Mockito.`when`(fm.beginTransaction()).thenReturn(ft)
            Mockito.`when`(ft.replace(id, fragment)).thenReturn(ft)
            assertSame(fragment, fm.findFragmentByIdOrReplace<FoodFragment>(id) { fragment })
            Mockito.verify(fm).beginTransaction()
            Mockito.verify(ft).replace(id, fragment)
            Mockito.verify(ft).commit()
        }
        run {
            val fm = Mockito.mock(FragmentManager::class.java)
            val fragment = FoodFragment()
            Mockito.`when`(fm.findFragmentById(id)).thenReturn(fragment)
            val exception = assertFailsWith<FragmentCastException> {
                fm.findFragmentByIdOrReplace<AnimalFragment>(id) { fail("should not be executed") }
            }
            assertEquals("fragment with ID $id was found, but was an instance of ${fragment::class} "
                    + "when expecting an instance of ${AnimalFragment::class}", exception.message)
        }
    }

    @SuppressLint("CommitTransaction")
    @Test
    fun test_findFragmentByTagOrAdd() {
        class FoodFragment : Fragment()
        class AnimalFragment : Fragment()

        val tag = "tag"

        run {
            val fm = Mockito.mock(FragmentManager::class.java)
            val fragment = FoodFragment()
            Mockito.`when`(fm.findFragmentByTag(tag)).thenReturn(fragment)
            assertSame(fragment, fm.findFragmentByTagOrAdd<FoodFragment>(tag) { fail("should not be executed") })
        }
        run {
            val fm = Mockito.mock(FragmentManager::class.java)
            val ft = Mockito.mock(FragmentTransaction::class.java)
            val fragment = FoodFragment()
            Mockito.`when`(fm.findFragmentByTag(tag)).thenReturn(null)
            Mockito.`when`(fm.beginTransaction()).thenReturn(ft)
            Mockito.`when`(ft.add(fragment, tag)).thenReturn(ft)
            assertSame(fragment, fm.findFragmentByTagOrAdd<FoodFragment>(tag) { fragment })
            Mockito.verify(fm).beginTransaction()
            Mockito.verify(ft).add(fragment, tag)
            Mockito.verify(ft).commit()
        }
        run {
            val fm = Mockito.mock(FragmentManager::class.java)
            val fragment = FoodFragment()
            Mockito.`when`(fm.findFragmentByTag(tag)).thenReturn(fragment)
            val exception = assertFailsWith<FragmentCastException> {
                fm.findFragmentByTagOrAdd<AnimalFragment>(tag) { fail("should not be executed") }
            }
            assertEquals("fragment with tag $tag was found, but was an instance of ${fragment::class} "
                    + "when expecting an instance of ${AnimalFragment::class}", exception.message)
        }
    }

}
