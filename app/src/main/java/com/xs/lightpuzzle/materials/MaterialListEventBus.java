package com.xs.lightpuzzle.materials;

import com.xs.lightpuzzle.data.entity.TemplateSet;

/**
 * Created by xs on 2018/11/8.
 */

public interface MaterialListEventBus {

    class SelectTemplate {

        private int position = -1;
        private final TemplateSet templateSet;

        SelectTemplate(TemplateSet templateSet) {
            this.templateSet = templateSet;
        }

        SelectTemplate(int position, TemplateSet templateSet) {
            this.position = position;
            this.templateSet = templateSet;
        }

        int getPosition() {
            return position;
        }

        TemplateSet getTemplateSet() {
            return templateSet;
        }
    }

    class DownloadingTemplate {

        private final int position;
        private final TemplateSet templateSet;

        public DownloadingTemplate(int position, TemplateSet templateSet) {
            this.position = position;
            this.templateSet = templateSet;
        }

        public int getPosition() {
            return position;
        }

        public TemplateSet getTemplateSet() {
            return templateSet;
        }
    }

    class DownloadedTemplate {

        private final int position;
        private final TemplateSet templateSet;

        public DownloadedTemplate(int position, TemplateSet templateSet) {
            this.position = position;
            this.templateSet = templateSet;
        }

        public int getPosition() {
            return position;
        }

        public TemplateSet getTemplateSet() {
            return templateSet;
        }
    }

    class UsedTemplate {

        private final int position;
        private final TemplateSet templateSet;

        UsedTemplate(int position, TemplateSet templateSet) {
            this.position = position;
            this.templateSet = templateSet;
        }

        int getPosition() {
            return position;
        }

        TemplateSet getTemplateSet() {
            return templateSet;
        }
    }

    class LikeTemplate {
        private final boolean isLike;
        private final TemplateSet templateSet;

        public LikeTemplate(boolean isLike, TemplateSet templateSet) {
            this.isLike = isLike;
            this.templateSet = templateSet;
        }

        public boolean isLike() {
            return isLike;
        }

        public TemplateSet getTemplateSet() {
            return templateSet;
        }
    }

    class DeleteTemplate {

        private int position = -1;
        private final TemplateSet templateSet;

        DeleteTemplate(TemplateSet templateSet) {
            this.templateSet = templateSet;
        }

        DeleteTemplate(int position, TemplateSet templateSet) {
            this.position = position;
            this.templateSet = templateSet;
        }

        int getPosition() {
            return position;
        }

        TemplateSet getTemplateSet() {
            return templateSet;
        }
    }

    class TemplateDeleted {

        private final int position;
        private final TemplateSet templateSet;

        TemplateDeleted(int position, TemplateSet templateSet) {
            this.position = position;
            this.templateSet = templateSet;
        }

        int getPosition() {
            return position;
        }

        TemplateSet getTemplateSet() {
            return templateSet;
        }
    }
}
