package org.kryptonmc.nbt.templates

import javax.inject.Inject
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.model.ObjectFactory

open class SourceSetTemplateExtensionImpl @Inject constructor(objects: ObjectFactory) : SourceSetTemplateExtension {

    override val templateSets: NamedDomainObjectContainer<TemplateSet> = objects.domainObjectContainer(TemplateSet::class.java)
}