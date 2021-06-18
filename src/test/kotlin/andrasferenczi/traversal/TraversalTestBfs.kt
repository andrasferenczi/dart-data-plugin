package andrasferenczi.traversal

import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class TraversalTestBfs {

    lateinit var root: Node<Int>
    lateinit var traversal: NodeTraversal<Int>

    // Collect all the values in which the children extract functions were called
    lateinit var childExtractCallbackCalls: MutableList<Node<Int>>

    @Before
    fun `create the tree`() {
        root = node(1) {
            node(2) {
                node(5)
            }
            node(3) {
                node(6)
                node(7) {
                    node(8)
                    node(9) {
                        node(11)
                    }
                    node(10)
                }
            }
            node(4)
        }

        childExtractCallbackCalls = LinkedList()
        traversal = createNodeTraversal(TraversalType.Breadth) { childExtractCallbackCalls.add(it) }
    }

    @Test
    fun `iterate all`() {
        val expected = (1..11).toList()
        val actual = traversal(root) { false }
            .map { node: Node<Int> -> node.value }
            .toList()

        val callbackValues = childExtractCallbackCalls.map { it.value }

        assertEquals(expected, actual)
        assertEquals(expected, callbackValues)
    }

    @Test
    fun `call first only`() {
        val expected = listOf(1)
        val actual = traversal(root) { true }
            .map { node: Node<Int> -> node.value }
            .toList()

        // No values
        val callbackValues = childExtractCallbackCalls.map { it.value }
        val expectedCallbacks = emptyList<Int>()

        assertEquals(expected, actual)
        assertEquals(expectedCallbacks, callbackValues)
    }

    @Test
    fun `path 1 to 3, then single depth`() {
        val expected = listOf(1, 2, 3, 4, 6, 7)
        val actual = traversal(root) {
            it.value !in setOf(1, 3)
        }
            .map { node: Node<Int> -> node.value }
            .toList()

        // No values
        val callbackValues = childExtractCallbackCalls.map { it.value }
        val expectedCallbacks = listOf(1, 3)

        assertEquals(expected, actual)
        assertEquals(expectedCallbacks, callbackValues)
    }

    @Test
    fun `path 1 to 3, then entire depth`() {
        val expected = listOf(1, 2, 3, 4, 6, 7, 8, 9, 10, 11)
        val actual = traversal(root) {
            // Not 2 or 4
            it.value in setOf(2, 4)
        }
            .map { node: Node<Int> -> node.value }
            .toList()

        // No values
        val callbackValues = childExtractCallbackCalls.map { it.value }
        val expectedCallbacks = listOf(1, 3, 6, 7, 8, 9, 10, 11)

        assertEquals(expected, actual)
        assertEquals(expectedCallbacks, callbackValues)
    }

}