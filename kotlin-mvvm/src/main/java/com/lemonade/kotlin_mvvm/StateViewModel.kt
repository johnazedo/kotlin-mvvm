package com.lemonade.kotlin_mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


open class PresentationError
open class State
open class Action

/**
 * FullViewModel is a pattern to create every view model with Live Data.
 *
 * The state is composed of all dynamic information (information that
 * could be changed) posted on screen.
 *
 * REMEMBER: The state must be a data class and the method .copy() must
 * be used to update only the target parameter.
 *
 * The action is composed of actions that will be perform something on screen, like show
 * a bottom sheet, close a fragment/activity, navigate to other screen etc...
 *
 * It is recommend use this with a sealed class or enum.
 */
open class FullViewModel<S: State, A: Action>(
    initialState: S
): StateViewModel<S>(initialState) {
    private val _action = MutableLiveData<OneShotWrapper<A>>()
    val getAction: LiveData<OneShotWrapper<A>> = _action

    protected fun performAction(block: () -> A) {
        val newAction = block()
        _action.value = OneShotWrapper(newAction)
    }
}

/**
 * StateViewModel is a pattern to create every view model with Live Data.
 *
 * The state is composed of all dynamic information (information that
 * could be changed) posted on screen.
 *
 * REMEMBER: The state must be a data class and the method .copy() must
 * be used to update only the target parameter.
 */
open class StateViewModel<S: State>(
    private val initialState: S
): ViewModel() {
    private val _state = MutableLiveData(initialState)
    val getState: LiveData<S> = _state

    private val _error = MutableLiveData<OneShotWrapper<PresentationError>>()
    val getError: LiveData<OneShotWrapper<PresentationError>> = _error

    protected fun updateState(block: () -> S) {
        val newState = block()
        _state.value = newState
    }

    protected fun showError(block: () -> PresentationError) {
        val newError = block()
        _error.value = OneShotWrapper(newError)
    }
}