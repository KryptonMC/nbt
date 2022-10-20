package org.kryptonmc.nbt.templates

import javax.inject.Inject
import net.kyori.mammoth.Configurable
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.model.ObjectFactory

open class SourceSetTemplateExtensionImpl @Inject constructor(objects: ObjectFactory) : SourceSetTemplateExtension {

    override val templateSets: NamedDomainObjectContainer<TemplateSet> = objects.domainObjectContainer(TemplateSet::class.java)
    internal var singleSetMode = false

    override fun singleSet(properties: Action<TemplateSet>) {
        singleSetMode = true
        Configurable.configure(templateSets.maybeCreate(SINGLE_SET_NAME), properties)
    }

    companion object {

        private const val SINGLE_SET_NAME = "main"
    }
}