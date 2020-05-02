def repo_path = 'https://github.com/other019/mrchecker-source'
multibranchPipelineJob('build'){
	branchSources{
		git{
			//id('1')
			remote(repo_path)
		}
	}
	orphanedItemStrategy{
		discardOldItems{
			numToKeep(25)
		}
	}
}
